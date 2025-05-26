package com.datastore.trade.command.service;

import com.datastore.trade.command.dto.TradeDtoUtil;
import com.datastore.trade.command.dto.TradeRequestDTO;
import com.datastore.trade.command.util.TradeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.repository.TradeRepository;
import com.datastore.trade.exception.ValidationException;
import com.datastore.trade.query.model.TradeHistory;
import com.datastore.trade.query.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.*;

@Service

@Slf4j
public class TradeStoreCommandService {

    private static final Logger logger = LoggerFactory.getLogger(TradeStoreCommandService.class);

    @Autowired
    private TradeRepository tradeRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    private TradeHistoryRepository tradeHistoryRepository;

    private final TradeVersionCacheService cacheService;

    @Autowired
    private ModelMapper modelMapper;

    private final ConcurrentHashMap<String, Integer> tradeVersionsMap = new ConcurrentHashMap<>();

    public TradeStoreCommandService(TradeVersionCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Async("tradeThreadPool")
    @Transactional
    public CompletableFuture<Void> processTradeAsync(Trade trade)
    {
        logger.info("Method processTradeAsync() :: START");

        //getExistingTradeMap();
        if(TradeValidator.validateTrade(trade,  cacheService.getLatestVersion(trade.getTradeId())))
            saveData(trade);

        logger.info("Method processTradeAsync() :: END");

        return CompletableFuture.completedFuture(null);
    }


    // another way using executorService
    @Transactional
    public void processTradeUsingExecutorService(TradeRequestDTO tradeRequestDTO) {
        logger.info("Processing Trade received via Kafka topics...processTradeUsingExecutorService() :: START");
        executor.submit(() -> {
            Trade trade = null;
                try {
                    trade = TradeDtoUtil.convertToEntity(tradeRequestDTO, modelMapper);
                }
                catch (ParseException pe)
                {
                    logger.error("Trade failed: ", pe.getMessage());
                }
                if(trade != null && TradeValidator.validateTrade(trade,  cacheService.getLatestVersion(trade.getTradeId())) )
                    saveData(trade);

            logger.info("Method processTradeUsingExecutorService() :: END");
        });

    }
    private void saveData(Trade trade)
    {
        logger.info("Method saveData() :: START");
        retrySaveTrade(trade, 3);//saves in MySql
        retrySaveTradeHistory(trade, 3);//saves each state of TRADE, can be used for further analytics

        cacheService.updateVersion(trade.getTradeId(), trade.getVersion()); // update the cache
        logger.info("Method saveData() :: END");
    }

    private void retrySaveTrade(Trade trade, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                tradeRepository.save(trade);
                //buildExistingTradeMap(trade);
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        logger.error("Failed to save trade after retries: {} ", trade.getTradeId());
    }

    private void retrySaveTradeHistory(Trade trade, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                TradeHistory historyDocument = tradeHistoryRepository.findById(trade.getTradeId())
                        .orElseGet(() -> {
                            TradeHistory doc = new TradeHistory();
                            doc.setTradeId(trade.getTradeId());
                            return doc;
                        });
                historyDocument.getHistory().add(trade);
                tradeHistoryRepository.save(historyDocument);
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        logger.error("Failed to save trade after retries: {} ", trade.getTradeId());
    }

    public List<Trade> getAllTrades()
    {
        logger.info("Inside getAllTrades()");
        return tradeRepository.findAll();
    }

    private void getExistingTradeMap()
    {
        logger.info("Method getExistingTradeMap():: START");
        List<Trade> tradeList;
        if(this.tradeVersionsMap.isEmpty()) {
            tradeList = getAllTrades();
            if(!tradeList.isEmpty())
            {
                for(Trade tt : tradeList)
                {
                    this.tradeVersionsMap.putIfAbsent(tt.getTradeId(), tt.getVersion());
                }
            }
        }
        logger.info("Method getExistingTradeMap():: END");

    }
    private void buildExistingTradeMap(Trade trade)
    {
        this.tradeVersionsMap.putIfAbsent(trade.getTradeId(), trade.getVersion());
    }

    private int getVersionId(String tradeId)
    {
        return this.tradeVersionsMap.isEmpty() ? 0 : this.tradeVersionsMap.getOrDefault(tradeId, 0);
    }

}
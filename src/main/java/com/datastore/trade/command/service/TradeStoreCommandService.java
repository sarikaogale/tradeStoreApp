package com.datastore.trade.command.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.repository.TradeRepository;
import com.datastore.trade.exception.ValidationException;
import com.datastore.trade.query.bean.TradeDetailsBean;
import com.datastore.trade.query.model.TradeHistory;
import com.datastore.trade.query.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeStoreCommandService {

    private static final Logger logger = LoggerFactory.getLogger(TradeStoreCommandService.class);

    @Autowired
    private TradeRepository tradeRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    @Autowired
    private  TradeHistoryRepository tradeHistoryRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Async("tradeThreadPool")
    @Transactional
    public CompletableFuture<Void> processTradeAsync(Trade trade)
    {

            // maintain each trade state., can be used for auditing, reporting etc.
            retrySaveTradeHistory(trade, 3);
            validateTrade(trade);

            retrySaveTrade(trade, 3);

           // cacheTrade(trade.getTradeId(), trade); // implement Redis-cache to handle in-memory data for faster execution.

        return CompletableFuture.completedFuture(null);
    }


    //another way using Executor Service, for testing purpose
    @Transactional
    public void processTradeUsingExecutorService(Trade trade) {
        executor.submit(() -> {
            try {
                // maintain each trade state., can be used for auditing, reporting etc.
                retrySaveTradeHistory(trade, 3);
                validateTrade(trade);
                retrySaveTrade(trade, 3);
            } catch (Exception e) {
                logger.error("Trade failed: ", e);
                System.err.println("Trade failed: " + e.getMessage());
            }
        });
    }

    private void validateTrade(Trade trade) {
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            logger.error("Trade maturity date is in the past");
            throw new ValidationException("Trade maturity date is in the past");
        }

        tradeRepository.findById(trade.getTradeId()).ifPresent(existing -> {
            if (trade.getVersion() < existing.getVersion()) {
                logger.error("Lower version trade received");
                throw new ValidationException("Lower version trade received");
            }
        });
    }

    private void retrySaveTrade(Trade trade, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                tradeRepository.save(trade);
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        logger.error("Failed to save trade after retries: {} ", trade.getTradeId());
        //System.err.println("Failed to save trade after retries: " + trade);
    }

    private void retrySaveTradeHistory(Trade trade, int retries) {

        for (int i = 0; i < retries; i++) {
            try {

                tradeHistoryRepository.save(new TradeHistory(trade.getTradeId(), trade));
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        logger.error("Failed to save trade after retries: {} ", trade.getTradeId());

        //System.err.println("Failed to save Trade History after retries: " + trade);
    }

    public void cacheTrade(String tradeId, Trade trade) {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            objMapper.registerModule(new JavaTimeModule());
            objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            redisTemplate.opsForValue().set(tradeId, objMapper.writeValueAsString(trade));
        } catch (Exception e) {
            logger.error("Error: ", e);
            throw new ValidationException(e.getMessage());
        }


    }
}
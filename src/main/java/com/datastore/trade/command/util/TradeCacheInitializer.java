package com.datastore.trade.command.util;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.repository.TradeRepository;
import com.datastore.trade.command.service.TradeVersionCacheService;
import com.datastore.trade.query.repository.TradeHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TradeCacheInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(TradeCacheInitializer.class);

    private final TradeRepository tradeRepository;
    private final TradeVersionCacheService cacheService;

    public TradeCacheInitializer(TradeRepository tradeRepository, TradeVersionCacheService cacheService) {
        this.tradeRepository = tradeRepository;
        this.cacheService = cacheService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //Set<String> existingKeys = redisTemplate.keys("*");

        if (cacheService.isCacheEmpty())  {
            logger.info("Redis cache empty. Loading trade versions ...");
            List <Trade> tradeList = tradeRepository.findAll();
            if(!tradeList.isEmpty())
            {
                for(Trade tt : tradeList)
                {
                    cacheService.updateVersion(tt.getTradeId(), tt.getVersion());
                }
            }
            logger.info("Trade versions loaded into Redis....");
        }
        else
            logger.info("Redis cache already contains data. Skipping pre-load.");
    }
}

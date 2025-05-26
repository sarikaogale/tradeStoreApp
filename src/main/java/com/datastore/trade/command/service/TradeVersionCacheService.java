package com.datastore.trade.command.service;

import com.datastore.trade.command.model.Trade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TradeVersionCacheService {

    private static final Logger logger = LoggerFactory.getLogger(TradeVersionCacheService.class);

    private final StringRedisTemplate redisTemplate;

    public TradeVersionCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Integer getLatestVersion(String tradeId) {
        String version = redisTemplate.opsForValue().get(tradeId);
        return version != null ? Integer.parseInt(version) : 0;
    }
    public void updateVersion(String tradeId, int version) {
        redisTemplate.opsForValue().set(tradeId, String.valueOf(version));
    }
    public boolean isCacheEmpty() {
        Set<String> keys = redisTemplate.keys("*");
        return keys == null || keys.isEmpty();    }

}

package com.datastore.trade.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");     // or use @Value("${spring.redis.host}")
        config.setPort(6379);
        // config.setPassword(RedisPassword.of("your_password")); // Uncomment if needed
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Use JSON serializer for values
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))  // default TTL
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));
    }
}
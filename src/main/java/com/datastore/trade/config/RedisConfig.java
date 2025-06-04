package com.datastore.trade.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;
   /* @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
        return new JedisConnectionFactory(config);
    }*/

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        // --- FIX IS HERE: Use the Docker service name 'redis_db' ---
      //  RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("redis_db", 6379);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);


        // If your Redis instance requires a password (as configured in docker-compose.yml),
        // you MUST set it here as well:
        // config.setPassword("your_redis_password"); // Replace with your actual Redis password

        return new JedisConnectionFactory(config);
    }
    @Bean
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(jedisConnectionFactory());
    }


}
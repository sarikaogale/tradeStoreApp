package com.datastore.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("classpath:application.yml")
@SpringBootApplication
@EnableScheduling
//@ComponentScan(basePackages = {"com.datastore.trade.command", "com.datastore.trade.query", "com.datastore.trade.config", "com.datastore.trade.exception"})
@ComponentScan(basePackages = {"com.datastore.trade", "com.producer.trade"})
public class TradeStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeStoreApplication.class, args);
    }

}

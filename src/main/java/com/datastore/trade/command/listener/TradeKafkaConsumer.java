package com.datastore.trade.command.listener;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.service.TradeStoreCommandService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TradeKafkaConsumer.class);

    private final TradeStoreCommandService tradeStoreCommandService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "trade-topic", groupId = "trade-group")
    public void consume(String tradeStr) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            Trade trade = objectMapper.readValue(tradeStr, Trade.class);
            logger.info("Consuming from Kafka topic: {}", trade.getTradeId());
            tradeStoreCommandService.processTradeAsync(trade);
        } catch (Exception e) {
            logger.error("Error while processing Trade from Kafka topic: ", e);
            throw new RuntimeException(e);
        }
    }
}

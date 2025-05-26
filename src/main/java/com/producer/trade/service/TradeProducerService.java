package com.producer.trade.service;

import com.datastore.trade.command.controller.TradeStoreCommandController;
import com.datastore.trade.command.dto.TradeRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeProducerService {

    private static final Logger logger = LoggerFactory.getLogger(TradeProducerService.class);

    private final KafkaTemplate<String, TradeRequestDTO> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;


    /**
     * This is used only for testing purpose ..... to send Trade
     * @param trade
     */
    public void sendTrade(TradeRequestDTO trade) {
        logger.info("Method: sendTrade() using Kafka producer :: START");
        kafkaTemplate.send(topic, trade.getTradeId(), trade);
        logger.info("Method: sendTrade() :: END");
    }
}
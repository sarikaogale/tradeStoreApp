package com.datastore.trade.command.listener;

import com.datastore.trade.command.dto.TradeRequestDTO;
import com.datastore.trade.command.service.TradeStoreCommandService;


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

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "trade-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(TradeRequestDTO message){
        logger.info("Method consume() :: START");
        tradeStoreCommandService.processTradeUsingExecutorService(message);
        logger.info("Method consume() :: END");
    }
}

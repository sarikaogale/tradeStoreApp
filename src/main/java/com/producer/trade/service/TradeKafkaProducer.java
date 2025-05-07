package com.producer.trade.service;

import com.datastore.trade.command.model.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This is used only for testing purpose ..... to send Trade
     * @param trade
     */
    public void sendTrade(Trade trade) {
        try {
            objectMapper.registerModule(new JavaTimeModule());

            String tradeJson = objectMapper.writeValueAsString(trade);
            kafkaTemplate.send("trade-topic", trade.getTradeId(), tradeJson);
            System.out.println("Trade sent to Kafka: " + tradeJson);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing trade: " + e.getMessage());
        }
    }
}
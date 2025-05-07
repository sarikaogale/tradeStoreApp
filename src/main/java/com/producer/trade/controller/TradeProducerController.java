package com.producer.trade.controller;

import com.datastore.trade.command.model.Trade;
import com.producer.trade.service.TradeKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class TradeProducerController {
    private final TradeKafkaProducer tradeKafkaProducer;

    @PostMapping("/send")
    public ResponseEntity<String> sendTrade(@RequestBody Trade trade) {
        tradeKafkaProducer.sendTrade(trade);
        return ResponseEntity.ok("Trade sent to Kafka topic");
    }
}

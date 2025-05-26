package com.producer.trade.controller;

import com.datastore.trade.command.dto.TradeRequestDTO;
import com.datastore.trade.command.model.Trade;
import com.producer.trade.service.TradeProducerService;
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
    private final TradeProducerService tradeProducerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTrade(@RequestBody TradeRequestDTO trade) {
        tradeProducerService.sendTrade(trade);
        return ResponseEntity.ok("Trade sent to Kafka topic");
    }
}

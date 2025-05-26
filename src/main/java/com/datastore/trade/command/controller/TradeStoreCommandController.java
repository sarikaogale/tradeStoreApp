package com.datastore.trade.command.controller;

import com.datastore.trade.command.dto.TradeRequestDTO;
import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.service.TradeStoreCommandService;
import com.datastore.trade.exception.ValidationException;
import com.producer.trade.service.TradeProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/datastore")
@RequiredArgsConstructor
public class TradeStoreCommandController
{
    private static final Logger logger = LoggerFactory.getLogger(TradeStoreCommandController.class);

    @Autowired
    private final TradeStoreCommandService tradeStoreCommandService;
    private final TradeProducerService tradeProducerService;

    @PostMapping("/trade")
    public Object addTrade(@RequestBody @Valid TradeRequestDTO dto) {
        Trade trade = new Trade(
                dto.getTradeId(),
                dto.getVersion(),
                dto.getCounterPartyId(),
                dto.getBookId(),
                dto.getMaturityDate(),
                dto.getCreatedDate(),
                dto.getExpired()
        );

        try {
            tradeStoreCommandService.processTradeAsync(trade);
        }
        catch (ValidationException ve)
        {
            return new ResponseEntity<>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ee)
        {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Trade is being processed");
    }
    @GetMapping
    public ResponseEntity<String> getTrade()
    {
        System.out.println("For testing only!!!");
        return ResponseEntity.ok("Inside the TRADE-STORE!!");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendTrade(@RequestBody TradeRequestDTO trade) {
        tradeProducerService.sendTrade(trade);
        return ResponseEntity.ok("Trade sent to Kafka topic");
    }
}

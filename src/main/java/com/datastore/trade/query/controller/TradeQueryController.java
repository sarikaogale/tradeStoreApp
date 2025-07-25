package com.datastore.trade.query.controller;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.query.model.TradeHistory;
import com.datastore.trade.query.service.TradeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeQueryController {

    private final TradeQueryService tradeQueryService;

    @GetMapping
    public List<TradeHistory> getAllTrades() {
        return tradeQueryService.getAllTrades();
    }

    @GetMapping("/{id}")
    public List<Trade> getTradeHistory(@PathVariable String id) {
        return tradeQueryService.getTradeHistory(id);
    }
}
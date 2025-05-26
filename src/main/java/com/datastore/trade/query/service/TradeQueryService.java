package com.datastore.trade.query.service;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.query.model.TradeHistory;
import com.datastore.trade.query.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeQueryService {
    private final TradeHistoryRepository repository;

    public List<Trade> getTradeHistory(String tradeId) {
        return repository.findById(tradeId)
                .map(TradeHistory::getHistory)
                .orElse(List.of());
    }

    public List<TradeHistory> getAllTrades()
    {
        return  repository.findAll();
    }

}
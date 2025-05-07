package com.datastore.trade.query.listener;

import com.datastore.trade.query.model.TradeHistory;
import com.datastore.trade.query.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeSyncConsumer {

    private final TradeHistoryRepository repository;

    /**
     * Implement this, configure consumer queue, where trade details will be sent and consumed
     * by the QueryService to store TRADE history
     * @param trade
     */
    @KafkaListener(topics = "trade-sync-topic", groupId = "query-group")
    public void syncTrade(TradeHistory trade) {
        repository.save(trade);
    }
}
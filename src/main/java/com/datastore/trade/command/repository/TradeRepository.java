package com.datastore.trade.command.repository;

import com.datastore.trade.command.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, String> {
    Optional<Trade> findByTradeIdAndVersion(String tradeId, int version);

    @Query("SELECT t FROM Trade t WHERE t.expired = 'N' AND t.maturityDate < CURRENT_DATE")
    List<Trade> findExpiredTrades();
}
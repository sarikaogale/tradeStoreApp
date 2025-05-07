package com.datastore.trade.command.service;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeExpiryService {

    private static final Logger logger = LoggerFactory.getLogger(TradeExpiryService.class);


    private final TradeRepository tradeRepository;
    @Scheduled(cron = "0 0 0 * * *")// Runs midnight, can be marked as every minute as required
    public void updateExpiredTrades() {
        List<Trade> expiredTrades = tradeRepository.findExpiredTrades();
        for (Trade trade : expiredTrades) {
            trade.setExpired("Y");
            tradeRepository.save(trade);
            logger.info("Trade expired: {} ", trade.getTradeId());
        }
    }

}

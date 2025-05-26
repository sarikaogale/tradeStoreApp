package com.datastore.trade.command.util;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TradeValidator {
    private static final Logger logger = LoggerFactory.getLogger(TradeValidator.class);

    public static boolean validateTrade(Trade trade,int version) {
        logger.info("Method validateTrade() :: START");

        AtomicBoolean retVal = new AtomicBoolean(true);
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            logger.error("Trade maturity date is in the past");
            retVal.set(false);
            throw new ValidationException("Trade maturity date is in the past");
        }
        if (trade.getVersion() < version) {
            logger.error("Lower version trade received");
            retVal.set(false);
            throw new ValidationException("Lower version trade received");
        }
        logger.info("Method validateTrade() :: END");
        return retVal.get();
    }

    public static boolean validateTrade2(Trade trade, Optional<Trade> existingTrade) {
        AtomicBoolean retVal = new AtomicBoolean(true);
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            logger.error("Trade maturity date is in the past");
            retVal.set(false);
            throw new ValidationException("Trade maturity date is in the past");
        }

        existingTrade.ifPresent(existing -> {
            if (trade.getVersion() < existing.getVersion()) {
                logger.error("Lower version trade received");
                retVal.set(false);
                throw new ValidationException("Lower version trade received");
            }
        });
        return retVal.get();
    }

    public static void validateTrade1(Trade trade, Optional<Trade> existingTrade) {
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            logger.error("Trade maturity date is in the past");
            throw new ValidationException("Trade maturity date is in the past");
        }

        existingTrade.ifPresent(existing -> {
            if (trade.getVersion() < existing.getVersion()) {
                logger.error("Lower version trade received");
                throw new ValidationException("Lower version trade received");
            }
        });
    }

}

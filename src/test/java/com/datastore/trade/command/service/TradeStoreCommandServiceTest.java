package com.datastore.trade.command.service;

import static org.mockito.Mockito.*;

import com.datastore.trade.command.model.Trade;
import com.datastore.trade.command.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TradeStoreCommandServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    private TradeStoreCommandService tradeCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tradeCommandService = new TradeStoreCommandService();
    }

    @Test
    void testProcessValidTrade() {
        Trade trade = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.now().plusDays(1), LocalDate.now(), "N");

        when(tradeRepository.findById("T1")).thenReturn(Optional.empty());

        try {
            tradeCommandService.processTradeAsync(trade);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(tradeRepository, atLeastOnce()).save(trade);
    }

    @Test
    void testRejectLowerVersionTrade() {
        Trade existing = new Trade("T1", 2, "CP-1", "B1",
                LocalDate.now().plusDays(1), LocalDate.now(), "N");

        Trade incoming = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.now().plusDays(1), LocalDate.now(), "N");

        when(tradeRepository.findById("T1")).thenReturn(Optional.of(existing));

        tradeCommandService.processTradeUsingExecutorService(incoming);

        verify(tradeRepository, never()).save(incoming);
    }
/*
    @InjectMocks
    private TradeStoreCommandService tradeStoreCommandService;
    @Mock
    private  TradeRepository tradeRepository;
    @Mock
    private TradeHistoryRepository tradeHistoryRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tradeRepository = mock(TradeRepository.class);
        tradeStoreCommandService = new TradeStoreCommandService();
    }

    @Test
    void TradeStoreCommandServiceTest()
    {
        Trade trade = new Trade("11", 11, "counterParty1212", "bookId1919",
                LocalDate.of(2026, Month.MAY, 7), LocalDate.of(2025, Month.MAY, 7), "N" );


        tradeStoreCommandService.processTradeUsingExecutorService(trade);
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void testAddTrade_Success() {
        Trade trade = new Trade("11", 11, "counterParty1212", "bookId1919",
                LocalDate.of(2026, Month.MAY, 7), LocalDate.of(2025, Month.MAY, 7), "N" );
        when(tradeRepository.save(trade)).thenReturn(trade);

        try {
            tradeStoreCommandService.processTradeAsync(trade);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(tradeRepository, verificationData -> new Trade("11", 11, "counterParty1212", "bookId1919",
                LocalDate.of(2026, Month.MAY, 7), LocalDate.of(2025, Month.MAY, 7), "N" ));

    }


    @Test
    void testAddTrade_NullTrade() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeStoreCommandService.processTradeUsingExecutorService(new Trade());
        });
        assertEquals("Trade cannot be null", exception.getMessage());
        verify(tradeRepository, never()).save(any());
    }
*/


}

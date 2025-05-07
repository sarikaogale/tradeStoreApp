package com.datastore.trade.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponseDTO {
    private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private String expired;
}

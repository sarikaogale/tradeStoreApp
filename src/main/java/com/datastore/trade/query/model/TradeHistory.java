package com.datastore.trade.query.model;


import com.datastore.trade.command.model.Trade;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("trade_history")
public class TradeHistory {
    @Id
    private String tradeId;
    private List<Trade> history = new ArrayList<>();
}
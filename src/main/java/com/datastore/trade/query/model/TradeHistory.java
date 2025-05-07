package com.datastore.trade.query.model;


import com.datastore.trade.query.bean.TradeDetailsBean;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("trade_history")
public class TradeHistory {
    @Id
    private String id;
    private String isExpired;
    private List<TradeDetailsBean> tradeDetailsBeanList;
}
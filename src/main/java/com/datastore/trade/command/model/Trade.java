package com.datastore.trade.command.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade_det")
public class Trade {
    @Id
    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "version")
    private int version;

    @Column(name = "counter_party_id")
    private String counterPartyId;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "maturity_date")
   // @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate maturityDate;

    @Column(name = "created_date")
   // @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    @Column(name = "is_expired")
    private String expired;
}

package com.datastore.trade.query.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDetailsBean
{
    @Min(1)
    private int version;

    @NotBlank
    private String counterPartyId;

    @NotBlank
    private String bookId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate maturityDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

}

package com.datastore.trade.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDTO {
    @NotBlank
    private String tradeId;

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

    private String expired = "N";
}
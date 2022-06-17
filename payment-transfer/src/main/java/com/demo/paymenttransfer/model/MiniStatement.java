package com.demo.paymenttransfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniStatement {
    private Integer accountId;
    private BigDecimal transactionAmount;
    private CurrencyType currency;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
}

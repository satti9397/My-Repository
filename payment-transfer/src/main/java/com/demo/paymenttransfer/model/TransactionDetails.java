package com.demo.paymenttransfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int senderId;

    private int receiverId;

    private LocalDateTime localDateTime;

    private BigDecimal txnAmount;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private String txnType;
}

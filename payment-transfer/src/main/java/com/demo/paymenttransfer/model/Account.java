package com.demo.paymenttransfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    @NotNull
    private Integer id;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}

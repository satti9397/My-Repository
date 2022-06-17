package com.demo.paymenttransfer;

import com.demo.paymenttransfer.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestSupportUtils {

    public static AccountInfoRequest getAccountInfoRequest() {
        return new AccountInfoRequest().builder()
                .accountId("111")
                .balance(new BigDecimal(1000))
                .currency(CurrencyType.GBP.name())
                .build();
    }

    public static Optional<Account> getOptionalAccountInfo() {
        Account accountInfo = new Account().builder()
                .id(111)
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static Optional<Account> getOptionalInactiveAccountInfo() {
        Account accountInfo = new Account().builder()
                .id(111)
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.INACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static Optional<Account> getOptionalAccountInfo2() {
        Account accountInfo = new Account().builder()
                .id(222)
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static List<Account> getAllAccountsDetails() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(getOptionalAccountInfo().get());
        accounts.add(getOptionalAccountInfo2().get());
        return accounts;
    }

    public static PaymentTransferRequest getPaymentTransferRequest() {
        PaymentTransferRequest paymentTransferRequest = new PaymentTransferRequest().builder()
                .senderId("111")
                .receiverId("222")
                .amount(new BigDecimal(20))
                .currency(CurrencyType.GBP.name())
                .build();

        return paymentTransferRequest;
    }

    public static List<TransactionDetails> getTransactionInfoList() {
        List<TransactionDetails> transactionInfoList = new ArrayList<>();

        transactionInfoList.add(new TransactionDetails().builder()
                .currencyType(CurrencyType.GBP)
                .senderId(111)
                .receiverId(222)
                .txnAmount(new BigDecimal(20))
                .localDateTime(LocalDateTime.now())
                .build());
        transactionInfoList.add(
                new TransactionDetails().builder()
                        .currencyType(CurrencyType.GBP)
                        .senderId(111)
                        .receiverId(222)
                        .txnAmount(new BigDecimal(30))
                        .localDateTime(LocalDateTime.now())
                        .build());

        transactionInfoList.add(
                new TransactionDetails().builder()
                        .currencyType(CurrencyType.GBP)
                        .senderId(222)
                        .receiverId(111)
                        .txnAmount(new BigDecimal(50))
                        .localDateTime(LocalDateTime.now())
                        .build());

        return transactionInfoList;
    }



}

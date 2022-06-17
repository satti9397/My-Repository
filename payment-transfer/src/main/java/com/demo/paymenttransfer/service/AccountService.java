package com.demo.paymenttransfer.service;

import com.demo.paymenttransfer.model.*;

import java.util.List;

public interface AccountService {
    void createAccount(AccountInfoRequest accountInfoRequest);

    Account getAccountDetails(String accountId);

    List<Account> getAllAccountDetails();

    TransactionDetails transferMoney(PaymentTransferRequest paymentTransferRequest);

    List<MiniStatement> getMiniStatement(String accountId);
}

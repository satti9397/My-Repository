package com.demo.paymenttransfer.service;

import com.demo.paymenttransfer.exception.CustomException;
import com.demo.paymenttransfer.model.*;
import com.demo.paymenttransfer.repository.AccountRepository;
import com.demo.paymenttransfer.repository.TransactionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;


    public void createAccount(AccountInfoRequest accountInfoRequest) {
        Account account = new Account().builder()
                .id(Integer.valueOf((accountInfoRequest.getAccountId())))
                .balance(accountInfoRequest.getBalance())
                .currencyType(CurrencyType.valueOf(accountInfoRequest.getCurrency()))
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        Optional<Account> existingAccount = accountRepository.findById(account.getId());
        if (existingAccount.isPresent()) {
            throw new CustomException("Account already exists with Account Id " + existingAccount.get().getId());
        }
        accountRepository.save(account);
    }

    public Account getAccountDetails(String accountId) {
        Optional<Account> accountInfo = accountRepository.findById(Integer.valueOf(accountId));
        if (accountInfo.isPresent()) {
            return accountInfo.get();
        }
        throw new CustomException("Account : " + accountId + " does not exists");
    }

    public List<Account> getAllAccountDetails() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new CustomException("No Account present");
        }
        return accounts;
    }

    @Transactional
    public TransactionDetails transferMoney(PaymentTransferRequest paymentTransferRequest) {
        Account senderAccount = validateSenderAccount(paymentTransferRequest);
        Account receiverAccount = validateReceiverAccount(paymentTransferRequest);

        //Debit from Sender Account
        senderAccount.setBalance(senderAccount.getBalance().subtract(paymentTransferRequest.getAmount()));
        //accountRepository.save(senderAccount);

        //Credit Receiver Account
        receiverAccount.setBalance(receiverAccount.getBalance().add(paymentTransferRequest.getAmount()));
        //accountRepository.save(receiverAccount);


        TransactionDetails transactionDetails = new TransactionDetails().builder()
                .senderId(senderAccount.getId())
                .receiverId(receiverAccount.getId())
                .txnAmount(paymentTransferRequest.getAmount())
                .localDateTime(LocalDateTime.now())
                .currencyType(CurrencyType.valueOf(paymentTransferRequest.getCurrency()))
                .build();

        //Log in transaction table
        transactionDetailsRepository.save(transactionDetails);
        return transactionDetails;
    }

    public List<MiniStatement> getMiniStatement(String accountId) {
        getAccountDetails(accountId);
        List<TransactionDetails> transactions = transactionDetailsRepository.findTransactionsForAccount(Integer.parseInt(accountId));
        List<MiniStatement> miniStatements = new ArrayList<>();
        for (TransactionDetails txn : transactions) {
            MiniStatement miniStatement = new MiniStatement().builder()
                    .accountId(txn.getSenderId() == Integer.parseInt(accountId) ? txn.getReceiverId() : txn.getSenderId())
                    .transactionAmount(txn.getTxnAmount())
                    .currency(txn.getCurrencyType())
                    .transactionType(txn.getSenderId() == Integer.parseInt(accountId) ? TransactionType.DEBIT : TransactionType.CREDIT)
                    .transactionTime(txn.getLocalDateTime())
                    .build();
            miniStatements.add(miniStatement);
        }
        return miniStatements;
    }

    /*Make sure Sender Account must exists and ACTIVE and balance amount must be more than transfer amount*/
    private Account validateSenderAccount(PaymentTransferRequest paymentTransferRequest) {
        Optional<Account> account = accountRepository.findById(Integer.valueOf(paymentTransferRequest.getSenderId()));
        if (!account.isPresent()) {
            throw new CustomException("Invalid Sender Account : " + paymentTransferRequest.getSenderId());
        }
        Account senderAccount = account.get();
        if (senderAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException("Sender Account : " + paymentTransferRequest.getSenderId() + " not Active");
        }

        if (!(senderAccount.getBalance().compareTo(paymentTransferRequest.getAmount()) > 0)) {
            throw new CustomException("Insufficient funds available");
        }
        return senderAccount;
    }

    /*Sender Account must be valid and ACTIVE */
    private Account validateReceiverAccount(PaymentTransferRequest paymentTransferRequest) {
        Optional<Account> account = accountRepository.findById(Integer.valueOf(paymentTransferRequest.getReceiverId()));
        if (!account.isPresent()) {
            throw new CustomException("Invalid Receiver Account : " + paymentTransferRequest.getReceiverId());
        }
        Account receiverAccount = account.get();
        if (receiverAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException("Receiver Account : " + paymentTransferRequest.getReceiverId() + " not Active");
        }
        return receiverAccount;
    }


}

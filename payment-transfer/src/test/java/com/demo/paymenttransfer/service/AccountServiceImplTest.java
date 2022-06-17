package com.demo.paymenttransfer.service;

import com.demo.paymenttransfer.TestSupportUtils;
import com.demo.paymenttransfer.exception.CustomException;
import com.demo.paymenttransfer.model.*;
import com.demo.paymenttransfer.repository.AccountRepository;
import com.demo.paymenttransfer.repository.TransactionDetailsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceImplTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createAccountIfAlreadyExists_failure() {
        when(accountRepository.findById(Mockito.anyInt())).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.createAccount(TestSupportUtils.getAccountInfoRequest());
        }, "Custom exception is expected");

        Assertions.assertEquals("Account already exists with Account Id 111", customException.getErrorMessage());
    }

    /*Fetch account details for an non existing account*/
    @Test
    void getAccountDetails_failure() {
        Optional<Account> emptyAccount = Optional.empty();
        when(accountRepository.findById(Mockito.anyInt())).thenReturn(emptyAccount);
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAccountDetails("111");
        }, "Custom exception is expected");

        Assertions.assertEquals("Account : 111 does not exists", customException.getErrorMessage());
    }

    /*Fetch account details for an non existing account*/
    @Test
    void getAccountDetails_success() {
        when(accountRepository.findById(Mockito.anyInt())).thenReturn(TestSupportUtils.getOptionalAccountInfo());

        Account account = accountService.getAccountDetails("111");

        Assertions.assertEquals(account.getId(), 111);
        Assertions.assertEquals(account.getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(account.getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(account.getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void getAllAccountDetails_success() {
        when(accountRepository.findAll()).thenReturn(TestSupportUtils.getAllAccountsDetails());
        List<Account> accountList = accountService.getAllAccountDetails();

        Assertions.assertEquals(accountList.get(0).getId(), 111);
        Assertions.assertEquals(accountList.get(0).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(0).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(0).getAccountStatus(), AccountStatus.ACTIVE);

        Assertions.assertEquals(accountList.get(1).getId(), 222);
        Assertions.assertEquals(accountList.get(1).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(1).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(1).getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void getAllAccountDetailsWhenNonePresent_failure() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAllAccountDetails();
        }, "Custom exception is expected");

        Assertions.assertEquals("No Account present", customException.getErrorMessage());
    }

    @Test
    void transferMoney_success() {
        PaymentTransferRequest paymentTransferRequest = new PaymentTransferRequest().builder()
                .senderId("111")
                .receiverId("222")
                .amount(new BigDecimal(20))
                .currency(CurrencyType.GBP.name())
                .build();
        when(accountRepository.findById(111)).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        when(accountRepository.findById(222)).thenReturn(TestSupportUtils.getOptionalAccountInfo2());
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(null);
        when(transactionDetailsRepository.save(Mockito.any(TransactionDetails.class))).thenReturn(null);
        //Mockito.doNothing().when(transactionDetailsRepository.save(Mockito.any()));

        TransactionDetails transactionDetails = accountService.transferMoney(paymentTransferRequest);

        Assertions.assertEquals(transactionDetails.getSenderId(), 111);
        Assertions.assertEquals(transactionDetails.getReceiverId(), 222);
        Assertions.assertEquals(transactionDetails.getTxnAmount(), new BigDecimal(20));
        Assertions.assertEquals(transactionDetails.getCurrencyType(), CurrencyType.GBP);
    }

    @Test
    void transferMoneyWhenSenderNotPresent_failure() {
        PaymentTransferRequest paymentTransferRequest = TestSupportUtils.getPaymentTransferRequest();
        Optional<Account> emptyAccount = Optional.empty();
        when(accountRepository.findById(111)).thenReturn(emptyAccount);
        when(accountRepository.findById(222)).thenReturn(TestSupportUtils.getOptionalAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.transferMoney(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid Sender Account : 111", customException.getErrorMessage());
    }

    @Test
    void transferMoneyWhenSenderIsInactive_failure() {
        PaymentTransferRequest paymentTransferRequest = TestSupportUtils.getPaymentTransferRequest();
        when(accountRepository.findById(111)).thenReturn(TestSupportUtils.getOptionalInactiveAccountInfo());
        when(accountRepository.findById(222)).thenReturn(TestSupportUtils.getOptionalAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.transferMoney(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Sender Account : 111 not Active", customException.getErrorMessage());
    }

    @Test
    void getMiniStatementForAccountId111() {
        when(accountRepository.findById(111)).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        when(transactionDetailsRepository.findTransactionsForAccount(111)).thenReturn(TestSupportUtils.getTransactionInfoList());

        List<MiniStatement> miniStatement = accountService.getMiniStatement("111");

        Assertions.assertEquals(miniStatement.size(), 3);

        Assertions.assertEquals(miniStatement.get(0).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(0).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(0).getTransactionAmount(), new BigDecimal(20));
        Assertions.assertEquals(miniStatement.get(0).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(1).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(1).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(1).getTransactionAmount(), new BigDecimal(30));
        Assertions.assertEquals(miniStatement.get(1).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(2).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(2).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(2).getTransactionAmount(), new BigDecimal(50));
        Assertions.assertEquals(miniStatement.get(2).getTransactionType(), TransactionType.CREDIT);

    }
}
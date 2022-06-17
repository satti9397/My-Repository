package com.demo.paymenttransfer.controller;

import com.demo.paymenttransfer.model.Account;
import com.demo.paymenttransfer.model.AccountStatus;
import com.demo.paymenttransfer.model.CurrencyType;
import com.demo.paymenttransfer.model.PaymentTransferRequest;
import com.demo.paymenttransfer.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*@SpringBootTest
@AutoConfigureMockMvc*/
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    private Account mockAccount1;

    private Account mockAccount2;

    private List<Account> accounts;

    @BeforeEach
    void setUp() {
        this.mockAccount1 = new Account().builder()
                .id(222)
                .balance(new BigDecimal("1000.00"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        this.mockAccount2 = new Account().builder()
                .id(111)
                .balance(new BigDecimal("1000.00"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        accounts = new ArrayList<>();
        accounts.add(mockAccount1);
        accounts.add(mockAccount2);
    }

    @Test
    void getAccountDetails_Unauthorized() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/account/111").accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized());
    }

    @Test
    void getAccountDetails_WrongHttpMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/account/111")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string(Matchers.containsString("Not the right Http Method")))
        ;
    }

    @Test
    void getAccountDetails_WrongUrl() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/wrong/111")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    void getAccountDetailsForValidAccountId() throws Exception {
        Mockito.when(accountService.getAccountDetails(Mockito.anyString())).thenReturn(mockAccount1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/account/111");
        mockHttpServletRequestBuilder.header("Authorization", "Basic YWRtaW46YWRtaW4=");
        String expected = "{\"id\":222,\"balance\":1000.00,\"currencyType\":\"GBP\",\"accountStatus\":\"ACTIVE\"}";

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void getAllAccounts() throws Exception {
        Mockito.when(accountService.getAllAccountDetails()).thenReturn(accounts);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/accounts")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        String expected = "[{\"id\":222,\"balance\":1000.00,\"currencyType\":\"GBP\",\"accountStatus\":\"ACTIVE\"},{\"id\":111,\"balance\":1000.00,\"currencyType\":\"GBP\",\"accountStatus\":\"ACTIVE\"}]";

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void transferMoney_SenderIdNull_Exception() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/accounts/transfer")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .content("{\n" +
                        " \"senderId\": null,\n" +
                        " \"receiverId\":222,\n" +
                        " \"amount\": 20,\n" +
                        " \"currency\":\"GBP\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockHttpServletRequestBuilder).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid senderId")));
    }

    @Test
    void transferMoney_Success() throws Exception {
        Mockito.when(accountService.transferMoney(Mockito.any(PaymentTransferRequest.class))).thenReturn(null);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/accounts/transfer")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .content("{\n" +
                        " \"senderId\": 111,\n" +
                        " \"receiverId\":222,\n" +
                        " \"amount\": 20,\n" +
                        " \"currency\":\"GBP\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
        ;

    }

}
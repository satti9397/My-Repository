package com.demo.paymenttransfer.controller;

import com.demo.paymenttransfer.exception.ErrorResponse;
import com.demo.paymenttransfer.model.AccountInfoRequest;
import com.demo.paymenttransfer.model.MiniStatement;
import com.demo.paymenttransfer.model.PaymentTransferRequest;
import com.demo.paymenttransfer.service.AccountService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@Validated
@Api(tags = "Account Services", description = "APIs to handle Account information and Balance related operations.")
public class AccountController {

   /* *//*Constructor injection*//*
    private AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }*/

    /*Setter injection*/
    @Autowired
    private AccountService accountService;


    @ApiOperation(value = "This API is used to create an Account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created successfully"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
    })
    @PostMapping("/accounts")
    public ResponseEntity<HttpStatus> createAccount(@ApiParam(value = "Account Details", required = true) @Valid @RequestBody AccountInfoRequest accountInfoRequest) {
        accountService.createAccount(accountInfoRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "This API is used to fetch Account details for a specific account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details fetched successfully", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
    })
    @GetMapping(value = "/account/{id}")
    public ResponseEntity<Object> getAccountDetails(@ApiParam(value = "Account Details", required = true, example = "111") @Valid @PathVariable("id") String accountId) {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }


    @ApiOperation(value = "This API is used to fetch Account details for all available accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details fetched successfully", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
    })
    @GetMapping("/accounts")
    public ResponseEntity<Object> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccountDetails(), HttpStatus.OK);
    }


    @ApiOperation(value = "This API is used to transfer amount between two internal accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transfer Successful", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
    })
    @PostMapping("/accounts/transfer")
    public ResponseEntity<HttpStatus> transferMoney(@ApiParam(value = "Transfer Request", required = true) @Valid @RequestBody PaymentTransferRequest paymentTransferRequest) {
        accountService.transferMoney(paymentTransferRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "This API is used to fetch Account details for all available accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details fetched successfully", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
    })
    @GetMapping("/accounts/{accountId}/mini")
    public ResponseEntity<Object> getMiniStatement(@ApiParam(value = "Account Id", required = true) @Valid @PathVariable String accountId) {
        List<MiniStatement> miniStatement = accountService.getMiniStatement(accountId);
        return new ResponseEntity<>(miniStatement, HttpStatus.OK);
    }
}

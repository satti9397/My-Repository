package com.demo.paymenttransfer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Account Information Request Body")
public class AccountInfoRequest {

    @ApiModelProperty(notes = "Account Id", required = true)
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String accountId;

    @ApiModelProperty(notes = "Account Balance", required = true)
    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal balance;

    @ApiModelProperty(notes = "Currency Type", required = true)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 3)
    private String currency;
}

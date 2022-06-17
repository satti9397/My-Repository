package com.demo.paymenttransfer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Account Information Request Body")
public class PaymentTransferRequest {

    @ApiModelProperty(notes = "Sender Id", required = true, position = 1)
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String senderId;

    @ApiModelProperty(notes = "Receiver Id", required = true, position = 2)
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String receiverId;

    @ApiModelProperty(notes = "Amount to be transferred", required = true, position = 3)
    @NotNull
    private BigDecimal amount;

    @ApiModelProperty(notes = "Type of Currency", required = true, position = 4)
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
}

package com.demo.paymenttransfer.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ErrorResponse {
    @ApiModelProperty(notes = "Failure Reason")
    private String errorMessage;
}

package com.demo.paymenttransfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class PaymentTransferControllerAdvice {

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpNotReadableException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid Request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleHttpMethodException(HttpRequestMethodNotSupportedException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Not the right Http Method"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid " + exception.getBindingResult().getFieldError().getField()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleInvalidRequest(ConstraintViolationException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid Request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

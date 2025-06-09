package com.snaphade.customerportfolio.advice;

import com.snaphade.customerportfolio.exceptions.CustomerNotFoundException;
import com.snaphade.customerportfolio.exceptions.InsufficientBalanceException;
import com.snaphade.customerportfolio.exceptions.InsufficientSharesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail invalidCustomerId(CustomerNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://example.com/invalid-customer-id"));
            problemDetail.setTitle("Invalid Customer Id");
        });

    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail insufficientBalance(InsufficientBalanceException ex){
        return build(HttpStatus.BAD_REQUEST, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://example.com/insufficient-Balance"));
            problemDetail.setTitle("Insufficient Balance");
        });

    }

    @ExceptionHandler(InsufficientSharesException.class)
    public ProblemDetail insufficientShares(InsufficientSharesException ex){
        return build(HttpStatus.BAD_REQUEST, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://example.com/insufficient-Shares-Quantity-to-sell"));
            problemDetail.setTitle("Insufficient Shares");
        });

    }


    private ProblemDetail build(HttpStatus httpStatus, Exception ex, Consumer<ProblemDetail> consumer){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        consumer.accept(problemDetail);
        return problemDetail;
    }
}

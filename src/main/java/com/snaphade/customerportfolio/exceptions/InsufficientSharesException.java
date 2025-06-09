package com.snaphade.customerportfolio.exceptions;

public class InsufficientSharesException extends RuntimeException{
    private static final String  MESSAGE = "Customer {id=%d} not have enough Shares to perform transaction" ;

    public InsufficientSharesException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}

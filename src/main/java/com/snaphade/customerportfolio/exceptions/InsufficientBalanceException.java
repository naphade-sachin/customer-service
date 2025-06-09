package com.snaphade.customerportfolio.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    private static final String  MESSAGE = "Customer {id=%d} not have enough funds to perform transaction" ;

    public InsufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}

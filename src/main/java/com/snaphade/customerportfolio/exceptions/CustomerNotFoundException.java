package com.snaphade.customerportfolio.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerNotFoundException  extends RuntimeException{
    private static final String  MESSAGE = "Customer {id=%d} not found" ;

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}

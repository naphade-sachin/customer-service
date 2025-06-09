package com.snaphade.customerportfolio.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {
    public static <T> Mono<T> customerNotFound(Integer custoomerId){
        return Mono.error(new CustomerNotFoundException(custoomerId));
    }

    public static <T> Mono<T> insufficientBalance(Integer custoomerId){
        return Mono.error(new InsufficientBalanceException(custoomerId));
    }

    public static <T> Mono<T> insufficientShares(Integer custoomerId){
        return Mono.error(new InsufficientSharesException(custoomerId));
    }
}

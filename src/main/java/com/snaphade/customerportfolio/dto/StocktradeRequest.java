package com.snaphade.customerportfolio.dto;

import com.snaphade.customerportfolio.domain.Ticker;
import com.snaphade.customerportfolio.domain.TradeAction;

public record StocktradeRequest(Ticker ticker, TradeAction action,Integer quantity,Integer price) {

    public Integer totalPrice(){
        return this.quantity*this.price;
    }
}

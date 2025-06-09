package com.snaphade.customerportfolio.dto;

import com.snaphade.customerportfolio.domain.Ticker;
import com.snaphade.customerportfolio.domain.TradeAction;

public record StockTradeResponse(Integer customerId,Integer balance, Ticker ticker, Integer price,
                                 Integer quantity, TradeAction action, Integer totalPrice) {
}

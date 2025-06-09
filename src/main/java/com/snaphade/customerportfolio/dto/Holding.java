package com.snaphade.customerportfolio.dto;

import com.snaphade.customerportfolio.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}

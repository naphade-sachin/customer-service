package com.snaphade.customerportfolio.entity;

import com.snaphade.customerportfolio.domain.Ticker;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("portfolio_item")
public class PortfolioItem {

    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CustomerPortfolio{" +
                "id=" + id +
                ", customer_id=" + customerId +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

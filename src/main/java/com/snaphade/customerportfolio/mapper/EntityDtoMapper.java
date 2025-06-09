package com.snaphade.customerportfolio.mapper;

import com.snaphade.customerportfolio.dto.CustomerInformationDTO;
import com.snaphade.customerportfolio.dto.Holding;
import com.snaphade.customerportfolio.dto.StockTradeResponse;
import com.snaphade.customerportfolio.dto.StocktradeRequest;
import com.snaphade.customerportfolio.entity.Customer;
import com.snaphade.customerportfolio.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerInformationDTO toDTO(Customer customer, List<PortfolioItem> portfolioList){
        var holding = portfolioList.stream()
                .map(folioItem -> new Holding(folioItem.getTicker(),folioItem.getQuantity()))
                .toList();
        return new CustomerInformationDTO(customer.getId(), customer.getName(), customer.getBalance(),holding);
    }

    public static PortfolioItem toPortFolioItem(Integer customerId, StocktradeRequest request){
        var folioItem =  new PortfolioItem();
        folioItem.setCustomerId(customerId);
        folioItem.setTicker(request.ticker());
        folioItem.setQuantity(0);
        return  folioItem;
    }

    public static StockTradeResponse toTradeResponseDto(Customer customer, StocktradeRequest request) {
        return new StockTradeResponse(customer.getId(),customer.getBalance(),request.ticker(),request.price(),
                request.quantity(),request.action(),request.totalPrice());
    }
}

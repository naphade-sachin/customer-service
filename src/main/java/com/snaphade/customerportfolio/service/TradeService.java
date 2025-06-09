package com.snaphade.customerportfolio.service;

import com.snaphade.customerportfolio.domain.TradeAction;
import com.snaphade.customerportfolio.dto.StockTradeResponse;
import com.snaphade.customerportfolio.dto.StocktradeRequest;
import com.snaphade.customerportfolio.entity.Customer;
import com.snaphade.customerportfolio.entity.PortfolioItem;
import com.snaphade.customerportfolio.exceptions.ApplicationExceptions;
import com.snaphade.customerportfolio.mapper.EntityDtoMapper;
import com.snaphade.customerportfolio.repository.CustomerRepository;
import com.snaphade.customerportfolio.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {
    private static Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StocktradeRequest stocktradeRequest){

        return switch (stocktradeRequest.action()) {
            case TradeAction.BUY -> this.buy(customerId, stocktradeRequest);
            case TradeAction.SELL -> this.sell(customerId, stocktradeRequest);
        };
    }

    private Mono<StockTradeResponse> buy(Integer customerId, StocktradeRequest stocktradeRequest) {
          /* Buy
        1) check if customer exist
        2) if not exist throw customer not found exception
        3) if found then check balance
        4)  check ticker and get its price from stockprice service, check total cost of trade
        5)  if total price is more than balance then insufficient balance exception
        6)  buy if balance is more than total
        7) increase quantity and reduce balance
           */

        var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .filter(customer -> customer.getBalance() >= stocktradeRequest.totalPrice())
                .switchIfEmpty(ApplicationExceptions.insufficientBalance(customerId));

        var portfolio = portfolioRepository.findByCustomerIdAndTicker(customerId, stocktradeRequest.ticker())
                .defaultIfEmpty(EntityDtoMapper.toPortFolioItem(customerId, stocktradeRequest));

        return customerMono.zipWhen(customer -> portfolio)
                .flatMap(t -> executeBuy(t.getT1(), t.getT2(), stocktradeRequest))
                ;
    }

    private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, StocktradeRequest request){
        customer.setBalance(customer.getBalance()-request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity()+request.quantity());

        return saveAndBuildResponse(customer, portfolioItem, request);
    }


    @Transactional
    private Mono<StockTradeResponse> sell(Integer customerId, StocktradeRequest request) {
          /*
                Sell Action
                1) check if customer exist, throw appropriate exceptions if not exist
                2) Check the ticker for the customer if exist in portfolio item if exist or not, throw appropriete exception
                3) Check quantity of ticker in request and portfolio item throw appropriet exception

        */

        var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));

        var portFolioItemMono = this.portfolioRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .filter(portfolioItem -> portfolioItem.getQuantity() >= request.quantity())
                .switchIfEmpty(ApplicationExceptions.insufficientShares(customerId))
                //.flatMap(portfolioItem -> checkTickerBalance(customerId, request, portfolioItem))
                ;

        //when both these mono present then only execute the sell action
        return customerMono.zipWhen(customer -> portFolioItemMono)
                .flatMap(t -> executeSell(t.getT1(), t.getT2(), request));

    }

    private Mono<StockTradeResponse> executeSell(Customer customer,PortfolioItem portfolioItem,StocktradeRequest request){
        customer.setBalance(customer.getBalance() + request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());

        return saveAndBuildResponse(customer, portfolioItem, request);
    }

    private Mono<StockTradeResponse> saveAndBuildResponse(Customer customer, PortfolioItem portfolioItem, StocktradeRequest request) {
        var response = EntityDtoMapper.toTradeResponseDto(customer, request);
        return Mono.zip(this.customerRepository.save(customer), this.portfolioRepository.save(portfolioItem))
                .thenReturn(EntityDtoMapper.toTradeResponseDto(customer, request));
    }

    private static Mono<PortfolioItem> checkTickerBalance(Integer customerId, StocktradeRequest request, PortfolioItem portfolioItem) {
        if(portfolioItem.getQuantity()< request.quantity()){
            return ApplicationExceptions.insufficientShares(customerId);
        }else {
            return Mono.just(portfolioItem);
        }
    }


}

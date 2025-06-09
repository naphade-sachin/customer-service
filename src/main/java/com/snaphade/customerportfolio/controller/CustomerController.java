package com.snaphade.customerportfolio.controller;

import com.snaphade.customerportfolio.dto.CustomerInformationDTO;
import com.snaphade.customerportfolio.dto.StockTradeResponse;
import com.snaphade.customerportfolio.dto.StocktradeRequest;
import com.snaphade.customerportfolio.service.CustomerService;
import com.snaphade.customerportfolio.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private static Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformationDTO> getCustomer(@PathVariable Integer customerId){
        return  this.customerService.getCustomerInformation(customerId);

    }


    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId,@RequestBody Mono<StocktradeRequest> request){
        return  request.flatMap(stocktradeRequest -> this.tradeService.trade(customerId,stocktradeRequest));
    }

}

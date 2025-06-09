package com.snaphade.customerportfolio.service;

import com.snaphade.customerportfolio.dto.CustomerInformationDTO;
import com.snaphade.customerportfolio.entity.Customer;
import com.snaphade.customerportfolio.exceptions.ApplicationExceptions;
import com.snaphade.customerportfolio.mapper.EntityDtoMapper;
import com.snaphade.customerportfolio.repository.CustomerRepository;
import com.snaphade.customerportfolio.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;


    public Mono<CustomerInformationDTO> getCustomerInformation(Integer customerId){
         return customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                  .flatMap(this::buildCustomerInformation)
                ;
    }

    private Mono<CustomerInformationDTO> buildCustomerInformation(Customer customer){
        return this.portfolioRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(portfolioItems -> EntityDtoMapper.toDTO(customer,portfolioItems));
    }

}

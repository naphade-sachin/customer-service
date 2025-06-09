package com.snaphade.customerportfolio.repository;

import com.snaphade.customerportfolio.domain.Ticker;
import com.snaphade.customerportfolio.entity.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioRepository extends ReactiveCrudRepository<PortfolioItem,Integer> {

    public Flux<PortfolioItem> findAllByCustomerId(Integer customerId);

    public Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);
}

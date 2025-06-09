package com.snaphade.customerportfolio;

import com.snaphade.customerportfolio.domain.Ticker;
import com.snaphade.customerportfolio.domain.TradeAction;
import com.snaphade.customerportfolio.dto.StocktradeRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {
 Logger log  = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);
	@Autowired
	private WebTestClient client;

	@Test
	void getCustomerInformation() {
		getCustomer(1,HttpStatus.OK)
				.jsonPath("$.name").isEqualTo("Sam")
				.jsonPath("$.balance").isEqualTo("10000")
				.jsonPath("$.holdings").isEmpty()
				;
	}

	@Test
	void BuyAndSaleTrade() {
		//Buy
		var request = new StocktradeRequest(Ticker.AMAZON, TradeAction.BUY, 5, 100);
		trade(2, request, HttpStatus.OK)
				.jsonPath("$.balance").isEqualTo(9500)
				.jsonPath("$.totalPrice").isEqualTo(500)
		;

		var request1 = new StocktradeRequest(Ticker.AMAZON, TradeAction.BUY, 10, 100);
		trade(2, request1, HttpStatus.OK)
				.jsonPath("$.balance").isEqualTo(8500)
				.jsonPath("$.totalPrice").isEqualTo(1000)
		;
		getCustomer(2, HttpStatus.OK)
				.jsonPath("$.holdings").isNotEmpty()
				.jsonPath("$.holdings.length()").isEqualTo(1)
				.jsonPath("$.holdings[0].ticker").isEqualTo("AMAZON")
				.jsonPath("$.holdings[0].quantity").isEqualTo("15")
		;
		//Sell

		var sellRequest = new StocktradeRequest(Ticker.AMAZON, TradeAction.SELL, 5, 110);
		trade(2, sellRequest, HttpStatus.OK)
				.jsonPath("$.balance").isEqualTo(9050)
				.jsonPath("$.totalPrice").isEqualTo(550)
		;

		var sellRequest2 = new StocktradeRequest(Ticker.AMAZON, TradeAction.SELL, 10, 110);
		trade(2, sellRequest2, HttpStatus.OK)
				.jsonPath("$.balance").isEqualTo(10150)
				.jsonPath("$.totalPrice").isEqualTo(1100)
		;
		getCustomer(2, HttpStatus.OK)
				.jsonPath("$.holdings").isNotEmpty()
				.jsonPath("$.holdings.length()").isEqualTo(1)
				.jsonPath("$.holdings[0].ticker").isEqualTo("AMAZON")
				.jsonPath("$.holdings[0].quantity").isEqualTo("0")
		;

	}


	@Test
	public void customerNotFoundtest(){
		getCustomer(10,HttpStatus.NOT_FOUND)
				.jsonPath("$.detail").isEqualTo("Customer {id=10} not found")
				;

		var sellRequest = new StocktradeRequest(Ticker.AMAZON, TradeAction.SELL, 5, 110);
		trade(10, sellRequest, HttpStatus.NOT_FOUND)
				.jsonPath("$.detail").isEqualTo("Customer {id=10} not found")
		;
	}

	@Test
	public void insufficientBalance(){
		var request = new StocktradeRequest(Ticker.AMAZON, TradeAction.BUY, 101, 100);
		trade(3, request, HttpStatus.BAD_REQUEST)
				.jsonPath("$.detail").isEqualTo("Customer {id=3} not have enough funds to perform transaction")
		;
	}


	@Test
	public void insufficientShares(){
		var request = new StocktradeRequest(Ticker.AMAZON, TradeAction.SELL, 101, 100);
		trade(3, request, HttpStatus.BAD_REQUEST)
				.jsonPath("$.detail").isEqualTo("Customer {id=3} not have enough Shares to perform transaction")
		;
	}

	private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus expectedStatus){
		return this.client.get()
				.uri("/customers/"+customerId)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectBody()
				.consumeWith(e -> log.info("{}"+ new String (e.getResponseBody())))
				;
	}


	private WebTestClient.BodyContentSpec trade(Integer customerId, StocktradeRequest request,HttpStatus expectedStatus){
		return this.client.post()
				.uri("/customers/"+customerId+"/trade")
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectBody()
				.consumeWith(e -> log.info("{}"+ new String (e.getResponseBody())))
				;
	}


}

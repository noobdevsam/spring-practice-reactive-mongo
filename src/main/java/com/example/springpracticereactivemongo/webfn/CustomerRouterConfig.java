package com.example.springpracticereactivemongo.webfn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CustomerRouterConfig {
	
	public static final String CUSTOMER_PATH = "/api/v3/customer";
	public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{id}";
	private final CustomerHandler customerHandler;
	
	public CustomerRouterConfig(CustomerHandler customerHandler) {
		this.customerHandler = customerHandler;
	}
	
	@Bean
	public RouterFunction<ServerResponse> createCustomerRouter() {
		return route()
			       .GET(CUSTOMER_PATH, accept(MediaType.APPLICATION_JSON), customerHandler::listCustomers)
			       .GET(CUSTOMER_PATH_ID, accept(MediaType.APPLICATION_JSON), customerHandler::getCustomerById)
			       .POST(CUSTOMER_PATH, accept(MediaType.APPLICATION_JSON), customerHandler::createCustomer)
			       .PUT(CUSTOMER_PATH_ID, accept(MediaType.APPLICATION_JSON), customerHandler::updateCustomer)
			       .PATCH(CUSTOMER_PATH_ID, accept(MediaType.APPLICATION_JSON), customerHandler::patchCustomer)
			       .DELETE(CUSTOMER_PATH_ID, accept(MediaType.APPLICATION_JSON), customerHandler::deleteCustomer)
			       .build();
	}
}

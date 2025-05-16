package com.example.springpracticereactivemongo.services;

import com.example.springpracticereactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
	
	Flux<CustomerDTO> listCustomers();
	
	Mono<CustomerDTO> getCustomerById(String id);
	
	Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO);
	
	Mono<CustomerDTO> createCustomer(Mono<CustomerDTO> customerDTO);
	
	Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDTO);
	
	Mono<CustomerDTO> patchCustomer(String id, CustomerDTO customerDTO);
	
	Mono<Void> deleteCustomerById(String id);
	
}

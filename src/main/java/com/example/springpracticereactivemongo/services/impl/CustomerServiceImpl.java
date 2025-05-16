package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.mappers.CustomerMapper;
import com.example.springpracticereactivemongo.model.CustomerDTO;
import com.example.springpracticereactivemongo.repositories.CustomerRepository;
import com.example.springpracticereactivemongo.services.CustomerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	
	public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
	}
	
	@Override
	public Flux<CustomerDTO> listCustomers() {
		return customerRepository.findAll()
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<CustomerDTO> getCustomerById(String id) {
		return customerRepository.findById(id)
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
		return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO))
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<CustomerDTO> createCustomer(Mono<CustomerDTO> customerDTO) {
		return customerDTO.map(customerMapper::customerDTOToCustomer)
			       .flatMap(customerRepository::save)
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDTO) {
		return getCustomerById(id)
			       .flatMap(found_customer ->
				                customerRepository.save(
					                new Customer(
						                found_customer.id(),
						                customerDTO.customerName(),
						                found_customer.createdDate(),
						                found_customer.lastModifiedDate()
					                )
				                )
			       )
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<CustomerDTO> patchCustomer(String id, CustomerDTO customerDTO) {
		return getCustomerById(id)
			       .flatMap(found_customer ->
				                customerRepository.save(
					                new Customer(
						                found_customer.id(),
						                customerDTO.customerName() != null ? customerDTO.customerName() : found_customer.customerName(),
						                found_customer.createdDate(),
						                found_customer.lastModifiedDate()
					                )
				                )
			       )
			       .map(customerMapper::customerToCustomerDTO);
	}
	
	@Override
	public Mono<Void> deleteCustomerById(String id) {
		return customerRepository.deleteById(id);
	}
}

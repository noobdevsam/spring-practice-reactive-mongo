package com.example.springpracticereactivemongo.mappers;

import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	CustomerDTO customerToCustomerDTO(Customer customer);
	
	Customer customerDTOToCustomer(CustomerDTO customerDTO);
}

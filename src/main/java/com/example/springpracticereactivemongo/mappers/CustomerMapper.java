package com.example.springpracticereactivemongo.mappers;

import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Customer and CustomerDTO objects.
 * Utilizes MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Converts a Customer entity to a CustomerDTO object.
     *
     * @param customer the Customer entity to convert
     * @return the corresponding CustomerDTO object
     */
    CustomerDTO customerToCustomerDTO(Customer customer);

    /**
     * Converts a CustomerDTO object to a Customer entity.
     *
     * @param customerDTO the CustomerDTO object to convert
     * @return the corresponding Customer entity
     */
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
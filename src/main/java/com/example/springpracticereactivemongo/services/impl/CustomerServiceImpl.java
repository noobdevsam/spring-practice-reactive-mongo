package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.mappers.CustomerMapper;
import com.example.springpracticereactivemongo.model.CustomerDTO;
import com.example.springpracticereactivemongo.repositories.CustomerRepository;
import com.example.springpracticereactivemongo.services.CustomerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the CustomerService interface.
 * Provides reactive methods for managing Customer entities, including CRUD operations and business logic.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Constructor for CustomerServiceImpl.
     *
     * @param customerRepository The repository for Customer entities.
     * @param customerMapper     The mapper for converting between Customer and CustomerDTO objects.
     */
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Retrieves a list of all customers.
     *
     * @return A Flux emitting all CustomerDTO objects.
     */
    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDTO);
    }

    /**
     * Retrieves a customer by its unique identifier.
     *
     * @param id The unique identifier of the customer.
     * @return A Mono emitting the CustomerDTO object, or empty if not found.
     */
    @Override
    public Mono<CustomerDTO> getCustomerById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO);
    }

    /**
     * Creates a new customer.
     *
     * @param customerDTO The CustomerDTO object to create.
     * @return A Mono emitting the created CustomerDTO object.
     */
    @Override
    public Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDTO);
    }

    /**
     * Creates a new customer using a reactive Mono wrapper.
     *
     * @param customerDTO A Mono containing the CustomerDTO object to create.
     * @return A Mono emitting the created CustomerDTO object.
     */
    @Override
    public Mono<CustomerDTO> createCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO.map(customerMapper::customerDTOToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDTO);
    }

    /**
     * Updates an existing customer.
     *
     * @param id          The unique identifier of the customer to update.
     * @param customerDTO The CustomerDTO object containing updated data.
     * @return A Mono emitting the updated CustomerDTO object, or empty if not found.
     */
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

    /**
     * Partially updates an existing customer.
     *
     * @param id          The unique identifier of the customer to patch.
     * @param customerDTO The CustomerDTO object containing partial updates.
     * @return A Mono emitting the patched CustomerDTO object, or empty if not found.
     */
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

    /**
     * Deletes a customer by its unique identifier.
     *
     * @param id The unique identifier of the customer to delete.
     * @return A Mono emitting void upon successful deletion.
     */
    @Override
    public Mono<Void> deleteCustomerById(String id) {
        return customerRepository.deleteById(id);
    }
}
package com.example.springpracticereactivemongo.services;

import com.example.springpracticereactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Customer entities.
 * Provides reactive methods for CRUD operations and additional business logic.
 */
public interface CustomerService {

    /**
     * Retrieves a list of all customers.
     *
     * @return A Flux emitting all CustomerDTO objects.
     */
    Flux<CustomerDTO> listCustomers();

    /**
     * Retrieves a customer by its unique identifier.
     *
     * @param id The unique identifier of the customer.
     * @return A Mono emitting the CustomerDTO object, or empty if not found.
     */
    Mono<CustomerDTO> getCustomerById(String id);

    /**
     * Creates a new customer.
     *
     * @param customerDTO The CustomerDTO object to create.
     * @return A Mono emitting the created CustomerDTO object.
     */
    Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO);

    /**
     * Creates a new customer using a reactive Mono wrapper.
     *
     * @param customerDTO A Mono containing the CustomerDTO object to create.
     * @return A Mono emitting the created CustomerDTO object.
     */
    Mono<CustomerDTO> createCustomer(Mono<CustomerDTO> customerDTO);

    /**
     * Updates an existing customer.
     *
     * @param id          The unique identifier of the customer to update.
     * @param customerDTO The CustomerDTO object containing updated data.
     * @return A Mono emitting the updated CustomerDTO object, or empty if not found.
     */
    Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDTO);

    /**
     * Partially updates an existing customer.
     *
     * @param id          The unique identifier of the customer to patch.
     * @param customerDTO The CustomerDTO object containing partial updates.
     * @return A Mono emitting the patched CustomerDTO object, or empty if not found.
     */
    Mono<CustomerDTO> patchCustomer(String id, CustomerDTO customerDTO);

    /**
     * Deletes a customer by its unique identifier.
     *
     * @param id The unique identifier of the customer to delete.
     * @return A Mono emitting void upon successful deletion.
     */
    Mono<Void> deleteCustomerById(String id);
}
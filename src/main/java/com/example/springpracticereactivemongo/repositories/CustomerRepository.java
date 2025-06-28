package com.example.springpracticereactivemongo.repositories;

import com.example.springpracticereactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Repository interface for managing Customer entities in a MongoDB database.
 * Extends the ReactiveMongoRepository to provide reactive CRUD operations.
 */
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
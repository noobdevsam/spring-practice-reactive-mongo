package com.example.springpracticereactivemongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a Customer entity stored in a MongoDB collection.
 * This class is implemented as a Java record, providing a compact syntax for immutable data objects.
 */
@Document
public record Customer(

        /**
         * The unique identifier for the Customer entity.
         */
        @Id
        String id,

        /**
         * The name of the customer.
         */
        String customerName,

        /**
         * The date and time when the customer entity was created.
         */
        LocalDateTime createdDate,

        /**
         * The date and time when the customer entity was last modified.
         */
        LocalDateTime lastModifiedDate
) {
    /**
     * Constructs a Customer instance with the specified name.
     * The ID, createdDate, and lastModifiedDate fields are set to null by default.
     *
     * @param customerName the name of the customer
     */
    public Customer(String customerName) {
        this(null, customerName, null, null);
    }
}
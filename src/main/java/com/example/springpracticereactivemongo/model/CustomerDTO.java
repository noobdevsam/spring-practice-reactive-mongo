package com.example.springpracticereactivemongo.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) representing a Customer entity.
 * This class uses Java's `record` feature to define immutable data objects.
 */
public record CustomerDTO(
		/**
		 * The unique identifier for the customer.
		 */
		String id,

		/**
		 * The name of the customer.
		 * Must not be blank.
		 */
		@NotBlank
		String customerName,

		/**
		 * The date and time when the customer was created.
		 * Optional field.
		 */
		LocalDateTime createdDate,

		/**
		 * The date and time when the customer was last modified.
		 * Optional field.
		 */
		LocalDateTime lastModifiedDate
) {
	/**
	 * Constructor for creating a CustomerDTO with only the customer name.
	 *
	 * @param customerName The name of the customer.
	 */
	public CustomerDTO(String customerName) {
		this(null, customerName, null, null);
	}
}
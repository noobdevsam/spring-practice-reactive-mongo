package com.example.springpracticereactivemongo.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CustomerDTO(
	String id,
	
	@NotBlank
	String customerName,
	
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
	public CustomerDTO(String customerName) {
		this(null, customerName, null, null);
	}
}
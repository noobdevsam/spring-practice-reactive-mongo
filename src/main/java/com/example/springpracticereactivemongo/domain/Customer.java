package com.example.springpracticereactivemongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public record Customer(
	
	@Id
	String id,
	String customerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
	public Customer(String customerName) {
		this(null, customerName, null, null);
	}
}

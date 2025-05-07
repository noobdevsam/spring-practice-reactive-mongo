package com.example.springpracticereactivemongo.domain;

import java.time.LocalDateTime;

public record Customer(
	String id,
	String customerName,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
	public Customer(String customerName) {
		this(null, customerName, null, null);
	}
}

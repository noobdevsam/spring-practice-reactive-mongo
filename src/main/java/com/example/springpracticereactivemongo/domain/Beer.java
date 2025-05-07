package com.example.springpracticereactivemongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
public record Beer(
	
	@Id
	String id,
	String beerName,
	String beerStyle,
	String upc,
	Integer quantityOnHand,
	BigDecimal price,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
	public Beer(
		String beerName,
		String beerStyle,
		String upc,
		Integer quantityOnHand,
		BigDecimal price
	) {
		this(null, beerName, beerStyle, upc, quantityOnHand, price, null, null);
	}
}

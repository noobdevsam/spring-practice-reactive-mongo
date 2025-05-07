package com.example.springpracticereactivemongo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Beer(
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

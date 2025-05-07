package com.example.springpracticereactivemongo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BeerDTO(
	String id,
	
	@NotBlank
	@Size(min = 3, max = 255)
	String beerName,
	
	@Size(min = 1, max = 255)
	String beerStyle,
	
	@Size(max = 25)
	String upc,
	Integer quantityOnHand,
	BigDecimal price,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
	public BeerDTO(
		String beerName
	) {
		this(null, beerName, null, null, null, null, null, null);
	}
	
	public BeerDTO(
		String beerName,
		String beerStyle,
		String upc,
		Integer quantityOnHand,
		BigDecimal price
	) {
		this(null, beerName, beerStyle, upc, quantityOnHand, price, null, null);
	}
}

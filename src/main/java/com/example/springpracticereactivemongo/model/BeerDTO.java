package com.example.springpracticereactivemongo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) representing a Beer entity.
 * This class uses Java's `record` feature to define immutable data objects.
 */
public record BeerDTO(
        /**
         * The unique identifier for the beer.
         */
        String id,

        /**
         * The name of the beer.
         * Must not be blank and must have a length between 3 and 255 characters.
         */
        @NotBlank
        @Size(min = 3, max = 255)
        String beerName,

        /**
         * The style of the beer.
         * Optional field with a maximum length of 255 characters.
         */
        @Size(min = 1, max = 255)
        String beerStyle,

        /**
         * The Universal Product Code (UPC) for the beer.
         * Optional field with a maximum length of 25 characters.
         */
        @Size(max = 25)
        String upc,

        /**
         * The quantity of beer available on hand.
         * Optional field.
         */
        Integer quantityOnHand,

        /**
         * The price of the beer.
         * Optional field.
         */
        BigDecimal price,

        /**
         * The date and time when the beer was created.
         * Optional field.
         */
        LocalDateTime createdDate,

        /**
         * The date and time when the beer was last modified.
         * Optional field.
         */
        LocalDateTime lastModifiedDate
) {
    /**
     * Constructor for creating a BeerDTO with only the beer name.
     *
     * @param beerName The name of the beer.
     */
    public BeerDTO(String beerName) {
        this(null, beerName, null, null, null, null, null, null);
    }

    /**
     * Constructor for creating a BeerDTO with an ID and beer name.
     *
     * @param id       The unique identifier for the beer.
     * @param beerName The name of the beer.
     */
    public BeerDTO(String id, String beerName) {
        this(id, beerName, null, null, null, null, null, null);
    }

    /**
     * Constructor for creating a BeerDTO with multiple fields.
     *
     * @param beerName       The name of the beer.
     * @param beerStyle      The style of the beer.
     * @param upc            The Universal Product Code (UPC) for the beer.
     * @param quantityOnHand The quantity of beer available on hand.
     * @param price          The price of the beer.
     */
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
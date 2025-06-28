package com.example.springpracticereactivemongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Beer entity stored in a MongoDB collection.
 * This class is implemented as a Java record, providing a compact syntax for immutable data objects.
 */
@Document
public record Beer(

        /**
         * The unique identifier for the Beer entity.
         */
        @Id
        String id,

        /**
         * The name of the beer.
         */
        String beerName,

        /**
         * The style or type of the beer (e.g., IPA, Pale Ale).
         */
        String beerStyle,

        /**
         * The Universal Product Code (UPC) for the beer.
         */
        String upc,

        /**
         * The quantity of beer available on hand.
         */
        Integer quantityOnHand,

        /**
         * The price of the beer.
         */
        BigDecimal price,

        /**
         * The date and time when the beer entity was created.
         */
        LocalDateTime createdDate,

        /**
         * The date and time when the beer entity was last modified.
         */
        LocalDateTime lastModifiedDate
) {
    /**
     * Constructs a Beer instance with the specified attributes, excluding the ID, createdDate, and lastModifiedDate.
     * These fields are set to null by default.
     *
     * @param beerName       the name of the beer
     * @param beerStyle      the style or type of the beer
     * @param upc            the Universal Product Code (UPC) for the beer
     * @param quantityOnHand the quantity of beer available on hand
     * @param price          the price of the beer
     */
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
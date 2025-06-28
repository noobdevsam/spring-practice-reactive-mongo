package com.example.springpracticereactivemongo.services;

import com.example.springpracticereactivemongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Beer entities.
 * Provides reactive methods for CRUD operations and additional business logic.
 */
public interface BeerService {

    /**
     * Retrieves all Beer entities.
     *
     * @return A Flux emitting all BeerDTO objects.
     */
    Flux<BeerDTO> findAll();

    /**
     * Retrieves a Beer entity by its unique identifier.
     *
     * @param id The unique identifier of the Beer entity.
     * @return A Mono emitting the BeerDTO object, or empty if not found.
     */
    Mono<BeerDTO> getBeerById(String id);

    /**
     * Finds the first Beer entity with the specified beer name.
     *
     * @param beerName The name of the beer to search for.
     * @return A Mono emitting the BeerDTO object, or empty if not found.
     */
    Mono<BeerDTO> findFirstByBeerName(String beerName);

    /**
     * Finds all Beer entities with the specified beer style.
     *
     * @param beerStyle The style of the beer to search for.
     * @return A Flux emitting all BeerDTO objects matching the beer style.
     */
    Flux<BeerDTO> findByBeerStyle(String beerStyle);

    /**
     * Saves a new Beer entity using a reactive Mono wrapper.
     *
     * @param beerDTO A Mono containing the BeerDTO object to save.
     * @return A Mono emitting the saved BeerDTO object.
     */
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);

    /**
     * Saves a new Beer entity.
     *
     * @param beerDTO The BeerDTO object to save.
     * @return A Mono emitting the saved BeerDTO object.
     */
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    /**
     * Updates an existing Beer entity.
     *
     * @param id      The unique identifier of the Beer entity to update.
     * @param beerDTO The BeerDTO object containing updated data.
     * @return A Mono emitting the updated BeerDTO object, or empty if not found.
     */
    Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO);

    /**
     * Partially updates an existing Beer entity.
     *
     * @param id      The unique identifier of the Beer entity to patch.
     * @param beerDTO The BeerDTO object containing partial updates.
     * @return A Mono emitting the patched BeerDTO object, or empty if not found.
     */
    Mono<BeerDTO> patchBeer(String id, BeerDTO beerDTO);

    /**
     * Deletes a Beer entity by its unique identifier.
     *
     * @param id The unique identifier of the Beer entity to delete.
     * @return A Mono emitting void upon successful deletion.
     */
    Mono<Void> deleteBeerById(String id);
}
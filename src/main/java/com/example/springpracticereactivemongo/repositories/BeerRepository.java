package com.example.springpracticereactivemongo.repositories;

import com.example.springpracticereactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for managing Beer entities in a MongoDB database.
 * Extends the ReactiveMongoRepository to provide reactive CRUD operations.
 */
public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

    /**
     * Finds the first Beer entity with the specified beer name.
     *
     * @param beerName The name of the beer to search for.
     * @return A Mono emitting the first Beer entity matching the beer name, or empty if none found.
     */
    Mono<Beer> findFirstByBeerName(String beerName);

    /**
     * Finds all Beer entities with the specified beer style.
     *
     * @param beerStyle The style of the beer to search for.
     * @return A Flux emitting all Beer entities matching the beer style.
     */
    Flux<Beer> findByBeerStyle(String beerStyle);
}
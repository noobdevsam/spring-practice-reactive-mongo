package com.example.springpracticereactivemongo.repositories;

import com.example.springpracticereactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
	
	Mono<Beer> findFirstByBeerName(String beerName);
}

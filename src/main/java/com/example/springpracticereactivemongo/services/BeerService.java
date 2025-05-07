package com.example.springpracticereactivemongo.services;

import com.example.springpracticereactivemongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
	
	Flux<BeerDTO> findAll();
	
	Mono<BeerDTO> getBeerById(String id);
	
	Mono<BeerDTO> createBeer(Mono<BeerDTO> beerDTO);
	
	Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
	
	Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO);
	
	Mono<BeerDTO> patchBeer(String id, BeerDTO beerDTO);
	
	Mono<Void> deleteBeerById(String id);
}

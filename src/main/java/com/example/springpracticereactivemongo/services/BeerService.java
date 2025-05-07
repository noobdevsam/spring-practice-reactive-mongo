package com.example.springpracticereactivemongo.services;

import com.example.springpracticereactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public interface BeerService {
	
	Mono<BeerDTO> getBeerById(String id);
	
	Mono<BeerDTO> createBeer(BeerDTO beerDTO);
}

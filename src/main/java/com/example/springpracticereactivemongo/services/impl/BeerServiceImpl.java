package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BeerServiceImpl implements BeerService {
	
	@Override
	public Mono<BeerDTO> getBeerById(String id) {
		return null;
	}
	
	@Override
	public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
		return null;
	}
	
}

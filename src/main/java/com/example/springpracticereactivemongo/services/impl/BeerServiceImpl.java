package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.mappers.BeerMapper;
import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BeerServiceImpl implements BeerService {
	
	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;
	
	public BeerServiceImpl(
		BeerRepository beerRepository,
		BeerMapper beerMapper
	) {
		this.beerRepository = beerRepository;
		this.beerMapper = beerMapper;
	}
	
	@Override
	public Mono<BeerDTO> getBeerById(String id) {
		return null;
	}
	
	@Override
	public Mono<BeerDTO> createBeer(Mono<BeerDTO> beerDTO) {
		return beerDTO
			       .map(beerMapper::beerDTOToBeer)
			       .flatMap(beerRepository::save)
			       .map(beerMapper::beerToBeerDTO);
	}
	
}

package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.mappers.BeerMapper;
import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
	public Flux<BeerDTO> findAll() {
		return beerRepository.findAll()
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<BeerDTO> getBeerById(String id) {
		return beerRepository.findById(id)
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<BeerDTO> createBeer(Mono<BeerDTO> beerDTO) {
		return beerDTO
			       .map(beerMapper::beerDTOToBeer)
			       .flatMap(beerRepository::save)
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
		return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO))
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO) {
		return getBeerById(id)
			       .flatMap(found_beer ->
				                beerRepository.save(
					                new Beer(
						                found_beer.id(),
						                beerDTO.beerName(),
						                beerDTO.beerStyle(),
						                beerDTO.upc(),
						                beerDTO.quantityOnHand(),
						                beerDTO.price(),
						                found_beer.createdDate(),
						                found_beer.lastModifiedDate()
					                )
				                ))
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<BeerDTO> patchBeer(String id, BeerDTO beerDTO) {
		return getBeerById(id)
			       .flatMap(found_beer ->
				                beerRepository.save(new Beer(
						                found_beer.id(),
						                beerDTO.beerName() != null ? beerDTO.beerName() : found_beer.beerName(),
						                beerDTO.beerStyle() != null ? beerDTO.beerStyle() : found_beer.beerStyle(),
						                beerDTO.upc() != null ? beerDTO.upc() : found_beer.upc(),
						                beerDTO.quantityOnHand() != null ? beerDTO.quantityOnHand() : found_beer.quantityOnHand(),
						                beerDTO.price() != null ? beerDTO.price() : found_beer.price(),
						                found_beer.createdDate(),
						                found_beer.lastModifiedDate()
					                )
				                ))
			       .map(beerMapper::beerToBeerDTO);
	}
	
	@Override
	public Mono<Void> deleteBeerById(String id) {
		return beerRepository.deleteById(id);
	}
	
}

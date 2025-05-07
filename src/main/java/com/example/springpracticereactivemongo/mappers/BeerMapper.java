package com.example.springpracticereactivemongo.mappers;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeerMapper {
	BeerDTO beerToBeerDTO(Beer beer);
	
	Beer beerDTOToBeer(BeerDTO beerDTO);
}

package com.example.springpracticereactivemongo.mappers;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Beer and BeerDTO objects.
 * Utilizes MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring")
public interface BeerMapper {

    /**
     * Converts a Beer entity to a BeerDTO object.
     *
     * @param beer the Beer entity to convert
     * @return the corresponding BeerDTO object
     */
    BeerDTO beerToBeerDTO(Beer beer);

    /**
     * Converts a BeerDTO object to a Beer entity.
     *
     * @param beerDTO the BeerDTO object to convert
     * @return the corresponding Beer entity
     */
    Beer beerDTOToBeer(BeerDTO beerDTO);
}
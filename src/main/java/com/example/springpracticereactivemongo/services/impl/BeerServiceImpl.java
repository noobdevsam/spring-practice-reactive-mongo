package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.mappers.BeerMapper;
import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the BeerService interface.
 * Provides reactive methods for managing Beer entities, including CRUD operations and business logic.
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    /**
     * Constructor for BeerServiceImpl.
     *
     * @param beerRepository The repository for Beer entities.
     * @param beerMapper     The mapper for converting between Beer and BeerDTO objects.
     */
    public BeerServiceImpl(
            BeerRepository beerRepository,
            BeerMapper beerMapper
    ) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    /**
     * Retrieves all Beer entities.
     *
     * @return A Flux emitting all BeerDTO objects.
     */
    @Override
    public Flux<BeerDTO> findAll() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Retrieves a Beer entity by its unique identifier.
     *
     * @param id The unique identifier of the Beer entity.
     * @return A Mono emitting the BeerDTO object, or empty if not found.
     */
    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Finds the first Beer entity with the specified beer name.
     *
     * @param beerName The name of the beer to search for.
     * @return A Mono emitting the BeerDTO object, or empty if not found.
     */
    @Override
    public Mono<BeerDTO> findFirstByBeerName(String beerName) {
        return beerRepository.findFirstByBeerName(beerName)
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Finds all Beer entities with the specified beer style.
     *
     * @param beerStyle The style of the beer to search for.
     * @return A Flux emitting all BeerDTO objects matching the beer style.
     */
    @Override
    public Flux<BeerDTO> findByBeerStyle(String beerStyle) {
        return beerRepository.findByBeerStyle(beerStyle)
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Saves a new Beer entity using a reactive Mono wrapper.
     *
     * @param beerDTO A Mono containing the BeerDTO object to save.
     * @return A Mono emitting the saved BeerDTO object.
     */
    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO
                .map(beerMapper::beerDTOToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Saves a new Beer entity.
     *
     * @param beerDTO The BeerDTO object to save.
     * @return A Mono emitting the saved BeerDTO object.
     */
    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO))
                .map(beerMapper::beerToBeerDTO);
    }

    /**
     * Updates an existing Beer entity.
     *
     * @param id      The unique identifier of the Beer entity to update.
     * @param beerDTO The BeerDTO object containing updated data.
     * @return A Mono emitting the updated BeerDTO object, or empty if not found.
     */
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

    /**
     * Partially updates an existing Beer entity.
     *
     * @param id      The unique identifier of the Beer entity to patch.
     * @param beerDTO The BeerDTO object containing partial updates.
     * @return A Mono emitting the patched BeerDTO object, or empty if not found.
     */
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

    /**
     * Deletes a Beer entity by its unique identifier.
     *
     * @param id The unique identifier of the Beer entity to delete.
     * @return A Mono emitting void upon successful deletion.
     */
    @Override
    public Mono<Void> deleteBeerById(String id) {
        return beerRepository.deleteById(id);
    }

}
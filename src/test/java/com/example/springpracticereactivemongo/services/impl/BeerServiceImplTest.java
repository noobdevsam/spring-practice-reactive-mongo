package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.services.BeerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {
	
	@Autowired
	BeerService beerService;
	
	@Autowired
	BeerRepository beerRepository;
	
	public static BeerDTO getTestBeerDTO() {
		return new BeerDTO("Space Dust", "IPA", "213514", 15, BigDecimal.TEN);
	}
	
	public BeerDTO getSavedBeerDTO() {
		return beerService.createBeer(Mono.just(getTestBeerDTO())).block();
	}
	
	@Test
	@DisplayName("Test save new beer using subscriber")
	void test_save_new_beer_use_subscriber() {
		var atomic_bool = new AtomicBoolean(false);
		var atomic_ref = new AtomicReference<BeerDTO>(); // for further testing
		var saved_mono = beerService.createBeer(Mono.just(getTestBeerDTO()));
		
		saved_mono.subscribe(saved -> {
			System.out.println("Saved beer: " + saved.toString());
			atomic_bool.set(true);
			atomic_ref.set(saved);
		});
		
		await().untilTrue(atomic_bool);
		
		var saved_beer = atomic_ref.get();
		assertThat(saved_beer).isNotNull();
		assertThat(saved_beer.id()).isNotNull();
	}
	
	@Test
	@DisplayName("Test save new beer using block")
	void test_save_new_beer_use_blocking() {
		var saved_dto = getSavedBeerDTO();
		assertThat(saved_dto).isNotNull();
		assertThat(saved_dto.id()).isNotNull();
	}
	
	@Test
	@DisplayName("Test find beer by id after updating")
	void test_update_blocking() {
		final String newName = "New Beer Name";
		var saved_dto = getSavedBeerDTO();
		
		var new_dto = new BeerDTO(newName, saved_dto.beerStyle(), saved_dto.upc(), saved_dto.quantityOnHand(), saved_dto.price());
		var updated_dto = beerService.updateBeer(saved_dto.id(), new_dto)
			                  .block();
		
		var fetched_dto = beerService.getBeerById(updated_dto.id()).block();
		assertThat(fetched_dto.beerName()).isEqualTo(newName);
	}
	
	@Test
	@DisplayName("Test find beer by id using subscriber")
	void test_update_streams() {
		final String newName = "New Beer Name";
		var atomic_dto = new AtomicReference<BeerDTO>();
		
		beerService.createBeer(Mono.just(getTestBeerDTO()))
			.map(saved_dto -> new BeerDTO(newName, saved_dto.beerStyle(), saved_dto.upc(), saved_dto.quantityOnHand(),
				saved_dto.price())
			)
			.flatMap(beerService::saveBeer)
			.flatMap(saved_updated_dto ->
				         // fetch the updated beer
				         beerService.getBeerById(saved_updated_dto.id())
			)
			.subscribe(dto_from_db -> atomic_dto.set(dto_from_db));
		
		await().until(() -> atomic_dto.get() != null);
		assertThat(atomic_dto.get().beerName()).isEqualTo(newName);
		
	}
	
}
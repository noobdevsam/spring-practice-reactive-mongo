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
}
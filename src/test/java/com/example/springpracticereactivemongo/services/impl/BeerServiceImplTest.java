package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {
	
	@Autowired
	BeerService beerService;
	
	public static BeerDTO getTestBeerDTO() {
		return new BeerDTO("Space Dust", "IPA", "213514", 15, BigDecimal.TEN);
	}
	
	@Test
	void test_save_new_beer() {
		var atomic_bool = new AtomicBoolean(false);
		var saved_mono = beerService.createBeer(Mono.just(getTestBeerDTO()));
		
		saved_mono.subscribe(saved -> {
			System.out.println("Saved beer: " + saved.toString());
			atomic_bool.set(true);
		});
		
		await().untilTrue(atomic_bool);
	}
}
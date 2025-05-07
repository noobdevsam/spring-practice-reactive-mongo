package com.example.springpracticereactivemongo.services.impl;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@SpringBootTest
class BeerServiceImplTest {
	
	@Autowired
	BeerService beerService;
	
	public static BeerDTO getTestBeerDTO() {
		return new BeerDTO("Space Dust", "IPA", "213514", 15, BigDecimal.TEN);
	}
	
	@Test
	void test_save_new_beer() throws InterruptedException {
		var saved_mono = beerService.createBeer(Mono.just(getTestBeerDTO()));
		
		saved_mono.subscribe(saved -> {
			System.out.println("Saved beer: " + saved.toString());
		});
		
		Thread.sleep(1000);
	}
}
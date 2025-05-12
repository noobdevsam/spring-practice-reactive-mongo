package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.BeerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerEndpointTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	@Test
	@Order(1)
	void test_list_beers() {
		webTestClient.get()
			.uri(BeerRouterConfig.BEER_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").value(greaterThan(1));
	}
	
	@Test
	@Order(2)
	void test_get_beer_by_id() {
		
		var beerDTO = getSavedTestBeer();
		
		webTestClient.get()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody(BeerDTO.class);
	}
	
	@Test
	@Order(3)
	void test_create_new_beer() {
		var testDTO = getSavedTestBeer();
		
		webTestClient.post()
			.uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(testDTO), BeerDTO.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().exists("location");
	}
	
	@Test
	@Order(4)
	void test_update_beer() {
		var beerDTO = getSavedTestBeer();
		
		webTestClient.put()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "Updated Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Test
	@Order(5)
	void test_patch_beer_by_id() {
		var beerDTO = getSavedTestBeer();
		
		webTestClient.patch()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "Patched Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	public BeerDTO getSavedTestBeer() {
		var beerDTOFluxExchangeResult = webTestClient.post()
			                                .uri(BeerRouterConfig.BEER_PATH)
			                                .body(Mono.just(new BeerDTO("Test Beer")), BeerDTO.class)
			                                .header("Content-type", "application/json")
			                                .exchange()
			                                .returnResult(BeerDTO.class);
		
		var location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");
		
		return webTestClient.get()
			       .uri(BeerRouterConfig.BEER_PATH)
			       .exchange()
			       .returnResult(BeerDTO.class)
			       .getResponseBody()
			       .blockFirst();
	}
	
}
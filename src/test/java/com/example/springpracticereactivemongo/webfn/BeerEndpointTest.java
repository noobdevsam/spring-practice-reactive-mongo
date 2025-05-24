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
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerEndpointTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	@Test
	@Order(1)
	void test_list_beers() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.get()
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
		webTestClient
			.mutateWith(mockOAuth2Login())
			.get()
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
		webTestClient
			.mutateWith(mockOAuth2Login())
			.post()
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
		webTestClient
			.mutateWith(mockOAuth2Login())
			.put()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "Updated Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Test
	@Order(5)
	void test_patch_beer_by_id() {
		var beerDTO = getSavedTestBeer();
		webTestClient
			.mutateWith(mockOAuth2Login())
			.patch()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "Patched Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Test
	@Order(6)
	void test_delete_beer_by_id() {
		var beerDTO = getSavedTestBeer();
		webTestClient
			.mutateWith(mockOAuth2Login())
			.delete()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Test
	@Order(7)
	void test_get_beer_by_id_not_found() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.get()
			.uri(BeerRouterConfig.BEER_ID_PATH, 1999)
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	@Order(8)
	void test_update_beer_not_found() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.put()
			.uri(BeerRouterConfig.BEER_ID_PATH, 1999)
			.body(Mono.just(new BeerDTO("1999", "Updated Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	@Order(9)
	void test_patch_beer_not_found() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.patch()
			.uri(BeerRouterConfig.BEER_ID_PATH, 1999)
			.body(Mono.just(new BeerDTO("1999", "Patched Beer")), BeerDTO.class)
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	@Order(10)
	void test_delete_beer_not_found() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.delete()
			.uri(BeerRouterConfig.BEER_ID_PATH, 1999)
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	@Order(11)
	void test_create_new_beer_bad_data() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.post()
			.uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(new BeerDTO("")), BeerDTO.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Test
	@Order(12)
	void test_update_beer_bad_data() {
		var beerDTO = getSavedTestBeer();
		webTestClient
			.mutateWith(mockOAuth2Login())
			.put()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "")), BeerDTO.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Test
	@Order(13)
	void test_patch_beer_bad_data() {
		var beerDTO = getSavedTestBeer();
		webTestClient
			.mutateWith(mockOAuth2Login())
			.patch()
			.uri(BeerRouterConfig.BEER_ID_PATH, beerDTO.id())
			.body(Mono.just(new BeerDTO(beerDTO.id(), "")), BeerDTO.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Test
	@Order(14)
	void test_list_beers_by_style() {
		webTestClient
			.mutateWith(mockOAuth2Login())
			.post()
			.uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(new BeerDTO("Test Beer 2", "TEST", "4689464", 8, BigDecimal.TEN)), BeerDTO.class)
			.header("Content-type", "application/json")
			.exchange();
		
		webTestClient
			.mutateWith(mockOAuth2Login())
			.get()
			.uri(
				UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH).queryParam("beerStyle", "TEST").build().toUri()
			)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").value(equalTo(1));
	}
	
	public BeerDTO getSavedTestBeer() {
		var beerDTOFluxExchangeResult = webTestClient
			                                .mutateWith(mockOAuth2Login())
			                                .post()
			                                .uri(BeerRouterConfig.BEER_PATH)
			                                .body(Mono.just(new BeerDTO("Test Beer")), BeerDTO.class)
			                                .header("Content-type", "application/json")
			                                .exchange()
			                                .returnResult(BeerDTO.class);
		
		var location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");
		
		return webTestClient
			       .mutateWith(mockOAuth2Login())
			       .get()
			       .uri(location.getFirst())
			       .exchange()
			       .returnResult(BeerDTO.class)
			       .getResponseBody()
			       .blockFirst();
	}
	
}


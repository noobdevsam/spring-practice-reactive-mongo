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

/**
 * Integration tests for the Beer API endpoints.
 * This class tests various operations such as listing, retrieving, creating, updating, patching, and deleting beers.
 */
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    /**
     * Tests listing all beers.
     * Verifies that the response contains a list of beers with a size greater than 1.
     */
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

    /**
     * Tests retrieving a beer by its ID.
     * Verifies that the response contains the expected BeerDTO object.
     */
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

    /**
     * Tests creating a new beer.
     * Verifies that the response status is 201 Created and a location header is present.
     */
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

    /**
     * Tests updating a beer.
     * Verifies that the response status is 204 No Content.
     */
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

    /**
     * Tests patching a beer by its ID.
     * Verifies that the response status is 204 No Content.
     */
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

    /**
     * Tests deleting a beer by its ID.
     * Verifies that the response status is 204 No Content.
     */
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

    /**
     * Tests retrieving a beer by an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
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

    /**
     * Tests updating a beer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
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

    /**
     * Tests patching a beer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
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

    /**
     * Tests deleting a beer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
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

    /**
     * Tests creating a new beer with invalid data.
     * Verifies that the response status is 400 Bad Request.
     */
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

    /**
     * Tests updating a beer with invalid data.
     * Verifies that the response status is 400 Bad Request.
     */
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

    /**
     * Tests patching a beer with invalid data.
     * Verifies that the response status is 400 Bad Request.
     */
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

    /**
     * Tests listing beers by their style.
     * Verifies that the response contains beers matching the specified style.
     */
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

    /**
     * Saves a test BeerDTO object and retrieves it.
     *
     * @return the saved BeerDTO instance.
     */
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
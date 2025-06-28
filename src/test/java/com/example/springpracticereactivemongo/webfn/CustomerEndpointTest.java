package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.CustomerDTO;
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
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

/**
 * Integration tests for the Customer API endpoints.
 * This class tests various operations such as listing, retrieving, creating, updating, patching, and deleting customers.
 */
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerEndpointTest {

    @Autowired
    WebTestClient webClient;

    /**
     * Tests listing all customers.
     * Verifies that the response contains a list of customers with a size greater than 1.
     */
    @Test
    @Order(1)
    void test_list_customers() {
        webClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    /**
     * Tests retrieving a customer by its ID.
     * Verifies that the response contains the expected CustomerDTO object.
     */
    @Test
    @Order(2)
    void test_get_customer_by_id() {
        var dto = getSavedTestCustomer();

        webClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.id())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    /**
     * Tests creating a new customer.
     * Verifies that the response status is 201 Created and a location header is present.
     */
    @Test
    @Order(3)
    void test_create_customer() {
        webClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(new CustomerDTO("test 2")), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    /**
     * Tests updating a customer.
     * Verifies that the response status is 204 No Content.
     */
    @Test
    @Order(4)
    void test_update_customer() {
        var dto = getSavedTestCustomer();

        webClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.id())
                .body(Mono.just(new CustomerDTO("test 3")), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    /**
     * Tests deleting a customer.
     * Verifies that the response status is 204 No Content.
     */
    @Test
    @Order(5)
    void test_delete_customer() {
        var dto = getSavedTestCustomer();

        webClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.id())
                .exchange()
                .expectStatus().isNoContent();
    }

    /**
     * Tests retrieving a customer by an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
    @Test
    @Order(6)
    void test_get_customer_by_id_not_found() {
        webClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * Tests updating a customer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
    @Test
    @Order(7)
    void test_update_customer_not_found() {
        webClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(new CustomerDTO("test 4")), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * Tests updating a customer with invalid data.
     * Verifies that the response status is 400 Bad Request.
     */
    @Test
    @Order(8)
    void test_update_customer_invalid_data() {
        var dto = getSavedTestCustomer();

        webClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.id())
                .body(Mono.just(new CustomerDTO("")), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * Tests deleting a customer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
    @Test
    @Order(9)
    void test_delete_customer_not_found() {
        webClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * Tests patching a customer with an ID that does not exist.
     * Verifies that the response status is 404 Not Found.
     */
    @Test
    @Order(10)
    void test_patch_customer_not_found() {
        webClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(new CustomerDTO("test 5")), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    /**
     * Saves a test CustomerDTO object and retrieves it.
     *
     * @return the saved CustomerDTO instance.
     */
    public CustomerDTO getSavedTestCustomer() {
        var customerDTOFluxExchangeResult = webClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(new CustomerDTO("test")), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        var location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        if (location != null) {
            return webClient
                    .mutateWith(mockOAuth2Login())
                    .get()
                    .uri(location.getFirst())
                    .exchange()
                    .returnResult(CustomerDTO.class)
                    .getResponseBody().blockFirst();
        }

        return null;
    }
}
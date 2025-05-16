package com.example.springpracticereactivemongo.webfn;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerEndpointTest {
	
	@Autowired
	WebTestClient webClient;
	
	@Test
	void test_list_customers() {
		webClient.get()
			.uri(CustomerRouterConfig.CUSTOMER_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-Type", "application/json")
			.expectBody().jsonPath("$.size()").value(greaterThan(1));
	}
}
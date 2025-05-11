package com.example.springpracticereactivemongo.webfn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BeerRouterConfig {
	
	public static final String BEER_PATH = "/api/v3/beer";
	public static final String BEER_ID_PATH = BEER_PATH + "/{id}";
	private final BeerHandler beerHandler;
	
	public BeerRouterConfig(BeerHandler beerHandler) {
		this.beerHandler = beerHandler;
	}
	
	/**
	 * Configures the routing for beer-related HTTP requests.
	 *
	 * @return a `RouterFunction<ServerResponse>` that defines the routes for handling
	 * beer-related operations. The routes include:
	 * - A GET request to `BEER_PATH` to retrieve a list of beers, handled by `listBeers`.
	 * - A GET request to `BEER_ID_PATH` to retrieve a specific beer by its ID, handled by `getBeerById`.
	 * Both routes accept requests with `application/json` media type.
	 */
	@Bean
	public RouterFunction<ServerResponse> beerRoutes() {
		return route()
			       .GET(BEER_PATH, accept(MediaType.APPLICATION_JSON), beerHandler::listBeers)
			       .GET(BEER_ID_PATH, accept(MediaType.APPLICATION_JSON), beerHandler::getBeerById)
			       .build();
	}
	
}

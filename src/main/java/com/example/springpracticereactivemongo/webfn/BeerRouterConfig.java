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
	 * Defines the routing configuration for beer-related HTTP requests.
	 *
	 * @return a `RouterFunction<ServerResponse>` that maps HTTP GET requests
	 * to the `listBeers` handler method in `BeerHandler`.
	 * The route accepts requests with `application/json` media type
	 * and responds with a list of beers.
	 */
	@Bean
	public RouterFunction<ServerResponse> beerRoutes() {
		return route()
			       .GET(BEER_PATH, accept(MediaType.APPLICATION_JSON), beerHandler::listBeers)
			       .build();
	}
	
	
}

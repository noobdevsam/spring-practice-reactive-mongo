package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BeerHandler {
	
	private final BeerService beerService;
	
	public BeerHandler(BeerService beerService) {
		this.beerService = beerService;
	}
	
	
	/**
	 * Handles an HTTP GET request to retrieve a list of beers.
	 *
	 * @param request the incoming HTTP request
	 * @return a `Mono<ServerResponse>` containing the HTTP 200 response with the list of beers
	 * in the response body. The response body is populated with a reactive stream
	 * of `BeerDTO` objects provided by the `beerService.findAll()` method.
	 */
	public Mono<ServerResponse> listBeers(ServerRequest request) {
		return ServerResponse.ok()
			       .body(beerService.findAll(), BeerDTO.class);
	}
	
	/**
	 * Handles an HTTP GET request to retrieve a beer by its ID.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 * @return a `Mono<ServerResponse>` containing the HTTP 200 response with the beer details
	 * in the response body. The response body is populated with a `BeerDTO` object
	 * provided by the `beerService.getBeerById()` method.
	 */
	public Mono<ServerResponse> getBeerById(ServerRequest request) {
		return ServerResponse.ok()
			       .body(beerService.getBeerById(request.pathVariable("id")), BeerDTO.class);
	}
}

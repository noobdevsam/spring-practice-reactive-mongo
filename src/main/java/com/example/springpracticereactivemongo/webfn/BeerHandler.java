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
	/*
	 * The method, listBeers(), is part of the BeerHandler class, which is a Spring WebFlux handler component.
	 * This method is designed to handle HTTP requests and return a reactive response containing a list of beers.
	 * The method takes a ServerRequest object as its parameter, which represents the incoming HTTP request.
	 * It uses the ServerResponse.ok() method to create a response with an HTTP 200 status code, indicating a successful operation.
	 * The response body is populated using the body method, which takes two arguments: a Publisher
	 * (in this case, a Flux returned by beerService.findAll()) and the class type of the objects in the response (BeerDTO.class).
	 * */
}

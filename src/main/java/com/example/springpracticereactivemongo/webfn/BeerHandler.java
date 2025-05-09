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
}

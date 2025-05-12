package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
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
	
	/**
	 * Handles an HTTP POST request to create a new beer.
	 *
	 * @param request the incoming HTTP request containing the beer details in the request body
	 * @return a `Mono<ServerResponse>` containing the HTTP 201 response with the location
	 * of the newly created beer resource in the `Location` header.
	 * The beer details are extracted from the request body as a `BeerDTO` object
	 * and saved using the `beerService.saveBeer()` method.
	 */
	public Mono<ServerResponse> createNewBeer(ServerRequest request) {
		return beerService.saveBeer(request.bodyToMono(BeerDTO.class))
			       .flatMap(beerDTO ->
				                ServerResponse.created(UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_ID_PATH).build(beerDTO.id()))
					                .build()
			       );
	}
	
	/**
	 * Handles an HTTP PUT request to update an existing beer by its ID.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 *                and the updated beer details in the request body
	 * @return a `Mono<ServerResponse>` containing the HTTP 204 response indicating
	 * that the beer was successfully updated. The beer details are extracted
	 * from the request body as a `BeerDTO` object and updated using the
	 * `beerService.updateBeer()` method.
	 */
	public Mono<ServerResponse> updateBeerById(ServerRequest request) {
		return request.bodyToMono(BeerDTO.class)
			       .flatMap(
				       beerDTO -> beerService.updateBeer(request.pathVariable("id"), beerDTO)
			       )
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
}

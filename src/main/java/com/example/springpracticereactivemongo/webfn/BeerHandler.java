package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
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
	 * This method processes a GET request where the beer ID is provided as a path variable.
	 * It uses the `beerService.getBeerById` method to fetch the beer details. If the beer
	 * is found, it returns an HTTP 200 response with the beer details in the response body.
	 * If the beer is not found, it throws a `ResponseStatusException` with an HTTP 404 status.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 * @return a `Mono<ServerResponse>` containing:
	 *         - HTTP 200 response with the beer details in the response body if the beer is found.
	 *         - HTTP 404 response if the beer is not found.
	 */
	public Mono<ServerResponse> getBeerById(ServerRequest request) {
		return ServerResponse.ok()
			       .body(beerService.getBeerById(request.pathVariable("id"))
				             .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
				       BeerDTO.class);
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
			       .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP PATCH request to partially update an existing beer by its ID.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 *                and the partial beer details in the request body
	 * @return a `Mono<ServerResponse>` containing the HTTP 204 response indicating
	 * that the beer was successfully updated. The partial beer details are
	 * extracted from the request body as a `BeerDTO` object and updated using
	 * the `beerService.patchBeer()` method.
	 */
	public Mono<ServerResponse> patchBeerById(ServerRequest request) {
		return request.bodyToMono(BeerDTO.class)
			       .map(
				       beerDTO -> beerService.patchBeer(request.pathVariable("id"), beerDTO)
			       )
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP DELETE request to delete a beer by its ID.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 * @return a `Mono<ServerResponse>` containing the HTTP 204 response indicating
	 * that the beer was successfully deleted. The deletion is performed
	 * using the `beerService.deleteBeerById()` method.
	 */
	public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
		return beerService.deleteBeerById(request.pathVariable("id"))
			       .then(ServerResponse.noContent().build());
	}
}

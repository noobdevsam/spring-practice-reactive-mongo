package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.BeerDTO;
import com.example.springpracticereactivemongo.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BeerHandler {
	
	private final BeerService beerService;
	private final Validator validator;
	
	public BeerHandler(BeerService beerService, Validator validator) {
		this.beerService = beerService;
		this.validator = validator;
	}
	
	/**
	 * Validates the given BeerDTO object.
	 * <p>
	 * This method uses a Validator to validate the provided BeerDTO object.
	 * If validation errors are found, a ServerWebInputException is thrown
	 * with the details of the validation errors.
	 *
	 * @param beerDTO the BeerDTO object to validate
	 * @throws ServerWebInputException if validation errors are found
	 */
	private void validate(BeerDTO beerDTO) {
		Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
		validator.validate(beerDTO, errors);
		
		if (errors.hasErrors()) {
			throw new ServerWebInputException(errors.toString());
		}
	}
	
	
	/**
	 * Handles an HTTP GET request to retrieve a list of beers.
	 *
	 * This method processes a GET request to fetch a list of beers. If a query parameter
	 * "beerStyle" is provided, it filters the beers by the specified style using the
	 * `beerService.findByBeerStyle` method. Otherwise, it retrieves all beers using
	 * the `beerService.findAll` method. The response is returned as an HTTP 200 status
	 * with the list of beers in the response body.
	 *
	 * @param request the incoming HTTP request
	 * @return a `Mono<ServerResponse>` containing:
	 *         - HTTP 200 response with the list of beers in the response body.
	 *         - The response body is populated with a reactive stream of `BeerDTO` objects.
	 */
	public Mono<ServerResponse> listBeers(ServerRequest request) {
		Flux<BeerDTO> flux = request.queryParam("beerStyle")
			.map(beerService::findByBeerStyle)
			.orElseGet(beerService::findAll);
		
		return ServerResponse.ok()
			       .body(flux, BeerDTO.class);
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
			       .doOnNext(this::validate)
			       .flatMap(beerDTO ->
				                ServerResponse.created(UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_ID_PATH).build(beerDTO.id()))
					                .build()
			       );
	}
	
	/**
	 * Handles an HTTP PUT request to update an existing beer by its ID.
	 *
	 * This method processes a PUT request where the beer ID is provided as a path variable,
	 * and the updated beer details are included in the request body. It uses the
	 * `beerService.updateBeer` method to update the beer details. If the beer is not found,
	 * it throws a `ResponseStatusException` with an HTTP 404 status.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 *                and the updated beer details in the request body
	 * @return a `Mono<ServerResponse>` containing:
	 *         - HTTP 204 response indicating that the beer was successfully updated.
	 *         - HTTP 404 response if the beer is not found.
	 */
	public Mono<ServerResponse> updateBeerById(ServerRequest request) {
		return request.bodyToMono(BeerDTO.class)
			       .doOnNext(this::validate)
			       .flatMap(
				       beerDTO -> beerService.updateBeer(request.pathVariable("id"), beerDTO)
			       )
			       .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP PATCH request to partially update an existing beer by its ID.
	 *
	 * This method processes a PATCH request where the beer ID is provided as a path variable,
	 * and the partial beer details are included in the request body. It uses the
	 * `beerService.patchBeer` method to apply the partial updates to the beer details.
	 * If the beer is not found, it throws a `ResponseStatusException` with an HTTP 404 status.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 *                and the partial beer details in the request body
	 * @return a `Mono<ServerResponse>` containing:
	 *         - HTTP 204 response indicating that the beer was successfully updated.
	 *         - HTTP 404 response if the beer is not found.
	 */
	public Mono<ServerResponse> patchBeerById(ServerRequest request) {
		return request.bodyToMono(BeerDTO.class)
			       .doOnNext(this::validate)
			       .flatMap(
				       beerDTO -> beerService.patchBeer(request.pathVariable("id"), beerDTO)
			       )
			       .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP DELETE request to delete a beer by its ID.
	 *
	 * This method processes a DELETE request where the beer ID is provided as a path variable.
	 * It first checks if the beer exists using the `beerService.getBeerById` method. If the beer
	 * is not found, it throws a `ResponseStatusException` with an HTTP 404 status. If the beer
	 * exists, it proceeds to delete the beer using the `beerService.deleteBeerById` method.
	 *
	 * @param request the incoming HTTP request containing the beer ID as a path variable
	 * @return a `Mono<ServerResponse>` containing:
	 *         - HTTP 204 response indicating that the beer was successfully deleted.
	 *         - HTTP 404 response if the beer is not found.
	 */
	public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
		return beerService.getBeerById(request.pathVariable("id"))
			       .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
			       .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.id()))
			       .then(ServerResponse.noContent().build());
	}
}

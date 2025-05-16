package com.example.springpracticereactivemongo.webfn;

import com.example.springpracticereactivemongo.model.CustomerDTO;
import com.example.springpracticereactivemongo.services.CustomerService;
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
import reactor.core.publisher.Mono;

@Component
public class CustomerHandler {
	
	private final CustomerService customerService;
	private final Validator validator;
	
	public CustomerHandler(CustomerService customerService, Validator validator) {
		this.customerService = customerService;
		this.validator = validator;
	}
	
	/**
	 * Validates the given CustomerDTO object.
	 * <p>
	 * This method uses a Validator to validate the provided CustomerDTO object.
	 * If validation errors are found, a ServerWebInputException is thrown
	 * with the details of the validation errors.
	 *
	 * @param customerDTO the CustomerDTO object to validate
	 * @throws ServerWebInputException if validation errors are found
	 */
	public void validate(CustomerDTO customerDTO) {
		Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDTO");
		validator.validate(customerDTO, errors);
		
		if (errors.hasErrors()) {
			throw new ServerWebInputException(errors.toString());
		}
	}
	
	/**
	 * Handles an HTTP GET request to retrieve a list of customers.
	 * <p>
	 * This method processes a GET request and returns an HTTP 200 response
	 * containing a reactive stream of `CustomerDTO` objects in the response body.
	 * The list of customers is fetched using the `customerService.listCustomers` method.
	 *
	 * @param request the incoming HTTP request
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 200 response with the list of customers in the response body.
	 * - The response body is populated with a reactive stream of `CustomerDTO` objects.
	 */
	public Mono<ServerResponse> listCustomers(ServerRequest request) {
		return ServerResponse.ok()
			       .body(
				       customerService.listCustomers(), CustomerDTO.class
			       );
	}
	
	/**
	 * Handles an HTTP GET request to retrieve a customer by ID.
	 * <p>
	 * This method processes a GET request to fetch a customer based on the provided ID
	 * in the request path. If no customer is found with the given ID, a `ResponseStatusException`
	 * with HTTP 404 status is thrown. The response is returned as an HTTP 200 status
	 * with the customer details in the response body.
	 *
	 * @param request the incoming HTTP request containing the customer ID in the path
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 200 response with the customer details in the response body.
	 * - HTTP 404 response if the customer is not found.
	 */
	public Mono<ServerResponse> getCustomerById(ServerRequest request) {
		return ServerResponse.ok()
			       .body(
				       customerService.getCustomerById(request.pathVariable("id"))
					       .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), CustomerDTO.class
			       );
	}
	
	/**
	 * Handles an HTTP POST request to create a new customer.
	 * <p>
	 * This method processes a POST request to create a new customer. The request body
	 * is expected to contain a `CustomerDTO` object, which is validated before being
	 * passed to the `customerService.createCustomer` method. Upon successful creation,
	 * the method returns an HTTP 201 response with the `Location` header pointing to
	 * the newly created customer's resource.
	 *
	 * @param request the incoming HTTP request containing the `CustomerDTO` in the body
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 201 response with the `Location` header set to the URI of the created customer.
	 */
	public Mono<ServerResponse> createCustomer(ServerRequest request) {
		return customerService.createCustomer(
				request.bodyToMono(CustomerDTO.class)
					.doOnNext(this::validate)
			)
			       .flatMap(
				       customerDTO -> ServerResponse.created(
					       UriComponentsBuilder.fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
						       .build(customerDTO.id())
				       ).build()
			       );
	}
	
	/**
	 * Handles an HTTP PUT request to update an existing customer.
	 * <p>
	 * This method processes a PUT request to update a customer based on the provided ID
	 * in the request path. The request body is expected to contain a `CustomerDTO` object,
	 * which is validated before being passed to the `customerService.updateCustomer` method.
	 * If no customer is found with the given ID, a `ResponseStatusException` with HTTP 404
	 * status is thrown. Upon successful update, the method returns an HTTP 204 response
	 * with no content.
	 *
	 * @param request the incoming HTTP request containing the customer ID in the path
	 *                and the `CustomerDTO` in the body
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 204 response with no content upon successful update.
	 * - HTTP 404 response if the customer is not found.
	 */
	public Mono<ServerResponse> updateCustomer(ServerRequest request) {
		return request.bodyToMono(CustomerDTO.class)
			       .doOnNext(this::validate)
			       .flatMap(
				       customerDTO -> customerService.updateCustomer(request.pathVariable("id"), customerDTO)
			       )
			       .switchIfEmpty(
				       Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
			       )
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP PATCH request to partially update an existing customer.
	 * <p>
	 * This method processes a PATCH request to update a customer based on the provided ID
	 * in the request path. The request body is expected to contain a `CustomerDTO` object,
	 * which is validated before being passed to the `customerService.patchCustomer` method.
	 * If no customer is found with the given ID, a `ResponseStatusException` with HTTP 404
	 * status is thrown. Upon successful update, the method returns an HTTP 204 response
	 * with no content.
	 *
	 * @param request the incoming HTTP request containing the customer ID in the path
	 *                and the `CustomerDTO` in the body
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 204 response with no content upon successful update.
	 * - HTTP 404 response if the customer is not found.
	 */
	public Mono<ServerResponse> patchCustomer(ServerRequest request) {
		return request.bodyToMono(CustomerDTO.class)
			       .doOnNext(this::validate)
			       .flatMap(
				       customerDTO -> customerService.patchCustomer(request.pathVariable("id"), customerDTO)
			       )
			       .switchIfEmpty(
				       Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
			       )
			       .flatMap(_ -> ServerResponse.noContent().build());
	}
	
	/**
	 * Handles an HTTP DELETE request to delete an existing customer.
	 * <p>
	 * This method processes a DELETE request to remove a customer based on the provided ID
	 * in the request path. If no customer is found with the given ID, a `ResponseStatusException`
	 * with HTTP 404 status is thrown. Upon successful deletion, the method returns an HTTP 204
	 * response with no content.
	 *
	 * @param request the incoming HTTP request containing the customer ID in the path
	 * @return a `Mono<ServerResponse>` containing:
	 * - HTTP 204 response with no content upon successful deletion.
	 * - HTTP 404 response if the customer is not found.
	 */
	public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
		return customerService.getCustomerById(request.pathVariable("id"))
			       .switchIfEmpty(
				       Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
			       )
			       .flatMap(
				       customerDTO -> customerService.deleteCustomerById(customerDTO.id())
			       )
			       .then(ServerResponse.noContent().build());
	}
	
}

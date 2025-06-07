package com.example.springpracticereactivemongo.config;


import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	/**
	 * Configures a `SecurityWebFilterChain` bean specifically for securing actuator endpoints.
	 * <p>
	 * This filter chain is applied with the highest priority (`@Order(1)`) and allows unrestricted
	 * access to all actuator endpoints. It uses the `EndpointRequest.toAnyEndpoint()` matcher
	 * to target all actuator endpoints.
	 *
	 * @param http the `ServerHttpSecurity` object used to configure the security filter chain
	 * @return a `SecurityWebFilterChain` that permits all access to actuator endpoints
	 * @throws Exception if an error occurs during the configuration
	 */
	@Bean
	@Order(1)
	public SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		return http
				.securityMatcher(
						EndpointRequest.toAnyEndpoint() // Matches all actuator endpoints
				)
				.authorizeExchange(
						auth -> auth.anyExchange().permitAll() // Permits all access to these endpoints
				)
				.build();
	}

	/**
	 * Configures a `SecurityWebFilterChain` bean for securing application endpoints.
	 *
	 * This filter chain is applied with a lower priority (`@Order(2)`) compared to the actuator
	 * security filter chain. It enforces authentication for all exchanges and configures the
	 * application to use JWT-based OAuth2 resource server support. CSRF protection is disabled
	 * for this configuration.
	 *
	 * @param http the `ServerHttpSecurity` object used to configure the security filter chain
	 * @return a `SecurityWebFilterChain` that enforces authentication and uses JWT for OAuth2
	 */
	@Bean
	@Order(2)
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange(
						exchange -> exchange.anyExchange().authenticated() // Requires authentication for all requests
			       )
				.oauth2ResourceServer(
						resource -> resource.jwt(Customizer.withDefaults()) // Configures JWT-based OAuth2 resource server
				)
				.csrf(ServerHttpSecurity.CsrfSpec::disable) // Disables CSRF protection
			       .build();
	}
}

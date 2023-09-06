package com.mamadou4bah.gatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route(p -> p
	            .path("/mbabank/accounts/**")
	            .filters(f -> f.filters(filterFactory.apply())
					.rewritePath("/mbabank/accounts/(?<segment>.*)","/${segment}")
					.removeRequestHeader("Cookie"))
	            .uri("lb://ACCOUNTS")).
	        route(p -> p
		            .path("/mbabank/loans/**")
					.filters(f -> f.filters(filterFactory.apply())
							.rewritePath("/mbabank/loans/(?<segment>.*)","/${segment}")
							.removeRequestHeader("Cookie"))
		            .uri("lb://LOANS")).
	        route(p -> p
		            .path("/mbabank/cards/**")
					.filters(f -> f.filters(filterFactory.apply())
							.rewritePath("/mbabank/cards/(?<segment>.*)","/${segment}")
							.removeRequestHeader("Cookie"))
		            .uri("lb://CARDS")).build();
	}

}

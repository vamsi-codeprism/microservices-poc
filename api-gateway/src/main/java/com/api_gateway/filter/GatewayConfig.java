package com.api_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth service (no filter)
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri("lb://auth-service"))

                // User service (secured)
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f
                                .rewritePath("/users/(?<segment>.*)", "/${segment}")
                                .filter(filter)
                        )
                        .uri("lb://user-service"))

                // Content service (secured)
                .route("content-service", r -> r.path("/content/**")
                        .filters(f -> f
                                .rewritePath("/content/(?<segment>.*)", "/${segment}")  // ✅ Rewrite path correctly
                                .filter(filter)
                        )
                        .uri("lb://content-service"))

                .build();
    }
}
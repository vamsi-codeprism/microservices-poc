package com.api_gateway.filter;

import com.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        System.out.println("Processing request: " + path);
        System.out.println("Headers: " + request.getHeaders());

        // Check if this route should be secured
        if (!validator.isSecured.test(request)) {
            System.out.println("Route is open, bypassing authentication: " + path);
            return chain.filter(exchange);
        }

        System.out.println("Route needs authentication: " + path);

        // Check for Authentication header
        if (authMissing(request)) {
            System.out.println("Authentication header missing for: " + path);
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        // Extract token
        final String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
        String token = authHeader;

        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // Validate token
        try {
            if (jwtUtil.isExpired(token)) {
                System.out.println("Token expired for: " + path);
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            System.out.println("Token valid, proceeding with request: " + path);

            // Add headers with user information if needed
            // ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
            //    .header("X-User-ID", jwtUtil.getClaims(token).getSubject())
            //    .build();
            // return chain.filter(exchange.mutate().request(modifiedRequest).build());

            return chain.filter(exchange);
        } catch (Exception e) {
            System.err.println("Error validating token for path " + path + ": " + e.getMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
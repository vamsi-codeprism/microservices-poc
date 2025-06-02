package com.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Prefixes that should bypass authentication
    private static final List<String> openApiPrefixes = List.of(
            "/auth/",
            "/eureka/"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        System.out.println("Checking security for path: " + path);

        boolean shouldBypass = openApiPrefixes.stream()
                .anyMatch(path::startsWith);  // matches /auth/, /users/, etc.

        System.out.println("Path " + path + " bypass security: " + shouldBypass);
        return !shouldBypass;  // if shouldBypass = true → NOT secured
    };
}

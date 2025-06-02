package com.content_service.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Replace `user-service` with the correct name from Eureka
@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}/exists")
    Boolean isUserExists(@PathVariable("id") Long userId);
}

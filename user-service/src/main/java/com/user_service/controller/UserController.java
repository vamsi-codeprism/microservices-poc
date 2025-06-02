package com.user_service.controller;

import com.user_service.dto.UserResponse;
import com.user_service.model.User;
import com.user_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(HttpServletRequest request) {
        System.out.println("🔹 [UserService] Received Auth Header: " +
                request.getHeader("Authorization"));
        List<UserResponse> users = userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody User user, HttpServletRequest request) {
        System.out.println("🔹 [UserService] Received Auth Header: " +
                request.getHeader("Authorization"));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    User updatedUser = userRepository.save(existingUser);
                    return ResponseEntity.ok(new UserResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
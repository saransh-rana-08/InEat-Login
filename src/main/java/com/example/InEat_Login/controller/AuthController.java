package com.example.InEat_Login.controller;

import com.example.InEat_Login.dto.AuthResponse;
import com.example.InEat_Login.dto.LoginRequest;
import com.example.InEat_Login.dto.RegisterRequest;
import com.example.InEat_Login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles HTTP requests for user authentication (registration and login).
 */
@RestController // Marks this class as a REST controller, ready to handle web requests.
@RequestMapping("/auth") // Maps all requests starting with "/auth" to this controller.
public class AuthController {

    private final UserService userService;

    // The UserService is injected via the constructor.
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for user registration.
     * @param registerRequest The user registration data from the request body.
     * @return A ResponseEntity with the authentication response or an error.
     */
    @PostMapping("/register") // Maps POST requests to /auth/register.
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // @Valid triggers validation on the request body.
            // @RequestBody binds the incoming JSON to the RegisterRequest object.
            AuthResponse response = userService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // If the service throws an error (e.g., email exists), return a 400 Bad Request.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint for user login.
     * @param loginRequest The user login credentials from the request body.
     * @return A ResponseEntity with the authentication response or an error.
     */
    @PostMapping("/login") // Maps POST requests to /auth/login.
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = userService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // For login failures, return a 401 Unauthorized status.
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
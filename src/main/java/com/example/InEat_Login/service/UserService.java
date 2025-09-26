package com.example.InEat_Login.service;

import com.example.InEat_Login.dto.AuthResponse;
import com.example.InEat_Login.dto.LoginRequest;
import com.example.InEat_Login.dto.RegisterRequest;
import com.example.InEat_Login.model.Role;
import com.example.InEat_Login.model.User;
import com.example.InEat_Login.repository.UserRepository;
import com.example.InEat_Login.security.JwtUtil;
// import org.springframework.security.crypto.password.PasswordEncoder; // <-- DELETE THIS IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder; // <-- DELETE THIS FIELD
    private final JwtUtil jwtUtil;

    // Update the constructor
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail().toLowerCase());

        // Store the password as plain text (INSECURE)
        user.setPassword(registerRequest.getPassword());

        user.setUniversityId(registerRequest.getUniversityId());
        try {
            user.setRole(Role.valueOf(registerRequest.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: Invalid role specified.");
        }
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check the plain text password (INSECURE)
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
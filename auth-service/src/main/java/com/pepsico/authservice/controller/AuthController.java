package com.pepsico.authservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.pepsico.authservice.util.JwtUtil;
import com.pepsico.authservice.exception.InvalidCredentialsException;

import java.util.Map;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${app.user.username}")
    private String validUsername;

    @Value("${app.user.password}")
    private String validPassword;

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param credentials Map containing username and password
     * @return JWT token if authentication is successful
     * @throws InvalidCredentialsException if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (validUsername.equals(username) && validPassword.equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }

}

package com.example.talent.controller;

import com.example.talent.application.AuthService;
import com.example.talent.config.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("Login attempt for email: {}", request.email());
        return authService.authenticate(request.email(), request.password())
                .map(user -> {
                    log.debug("Authentication successful for: {}", request.email());
                    String token = jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(new LoginResponse(user.getUserId(), user.getEmail(), user.getName(), token));
                })
                .orElseGet(() -> {
                    log.debug("Authentication failed for: {}", request.email());
                    String errorMessage = authService.getAuthenticationFailureReason(request.email());
                    return ResponseEntity.status(401).body(new ErrorResponse(errorMessage));
                });
    }

    record LoginRequest(String email, String password) {}
    record LoginResponse(String userId, String email, String name, String token) {}
    record ErrorResponse(String message) {}
}

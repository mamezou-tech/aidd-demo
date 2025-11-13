package com.example.demo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String message;
    private String token;
    private Long expiresIn;

    public AuthResponse(String message) {
        this.message = message;
    }
}

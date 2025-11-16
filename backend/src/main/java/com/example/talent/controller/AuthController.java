package com.example.talent.controller;

import com.example.talent.application.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        boolean authenticated = authenticationService.authenticate(request.username, request.password);
        
        if (authenticated) {
            session.setAttribute("username", request.username);
            return ResponseEntity.ok(Map.of("username", request.username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "ユーザーIDまたはパスワードが正しくありません。"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "ログアウトしました。"));
    }

    static class LoginRequest {
        public String username;
        public String password;
    }
}

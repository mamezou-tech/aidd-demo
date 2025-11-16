package com.example.talent.controller;

import com.example.talent.application.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthenticationService authenticationService,
                          SecurityContextRepository securityContextRepository) {
        this.authenticationService = authenticationService;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, 
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {
        boolean authenticated = authenticationService.authenticate(request.username, request.password);
        
        if (authenticated) {
            // Spring Securityの認証を設定
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(request.username, null, Collections.emptyList());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            
            // セッションに保存
            securityContextRepository.saveContext(context, httpRequest, httpResponse);
            
            return ResponseEntity.ok(Map.of("username", request.username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "ユーザーIDまたはパスワードが正しくありません。"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "ログアウトしました。"));
    }

    static class LoginRequest {
        public String username;
        public String password;
    }
}

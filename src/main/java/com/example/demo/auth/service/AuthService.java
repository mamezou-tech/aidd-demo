package com.example.demo.auth.service;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.SignupRequest;
import com.example.demo.auth.security.JwtTokenProvider;
import com.example.demo.common.exception.AuthException;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new AuthException("このメールアドレスは既に登録されています");
        }

        User user = userService.createUser(request.getEmail(), request.getPassword());
        
        return new AuthResponse("ユーザー登録が完了しました");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("メールアドレスまたはパスワードが正しくありません"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("メールアドレスまたはパスワードが正しくありません");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse("ログインに成功しました", token, jwtExpiration);
    }

    public AuthResponse logout(String token) {
        jwtTokenProvider.blacklistToken(token);
        return new AuthResponse("ログアウトしました");
    }
}

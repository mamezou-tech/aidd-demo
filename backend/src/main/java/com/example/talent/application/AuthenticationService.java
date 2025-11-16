package com.example.talent.application;

import com.example.talent.domain.User;
import com.example.talent.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.info("User found: {}, hash: {}", username, user.getPasswordHash());
                    boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
                    log.info("Password matches: {}", matches);
                    return matches;
                })
                .orElseGet(() -> {
                    log.warn("User not found: {}", username);
                    return false;
                });
    }
}

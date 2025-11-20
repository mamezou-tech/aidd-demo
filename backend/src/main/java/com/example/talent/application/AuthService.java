package com.example.talent.application;

import com.example.talent.domain.model.User;
import com.example.talent.infrastructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> authenticate(String email, String password) {
        log.debug("Authenticating user: {}", email);
        Optional<User> result = userRepository.findByEmail(email)
                .map(userEntity -> {
                    User user = userEntity.toDomain();
                    log.debug("User found: {}, checking password", email);
                    boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
                    log.debug("Password match result: {}", matches);
                    return matches ? user : null;
                })
                .filter(user -> user != null);
        
        if (result.isEmpty()) {
            log.debug("Authentication failed - user not found or password mismatch");
        }
        return result;
    }
}

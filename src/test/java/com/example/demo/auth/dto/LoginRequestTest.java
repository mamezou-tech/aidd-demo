package com.example.demo.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 有効なリクエスト() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void メールアドレスが空() {
        LoginRequest request = new LoginRequest("", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void メールアドレスの形式が無効() {
        LoginRequest request = new LoginRequest("invalid-email", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void パスワードが空() {
        LoginRequest request = new LoginRequest("test@example.com", "");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }
}

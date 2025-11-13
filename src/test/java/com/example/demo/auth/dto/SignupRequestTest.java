package com.example.demo.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 有効なリクエスト() {
        SignupRequest request = new SignupRequest("test@example.com", "password123");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void メールアドレスが空() {
        SignupRequest request = new SignupRequest("", "password123");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("メールアドレスは必須です"));
    }

    @Test
    void メールアドレスがnull() {
        SignupRequest request = new SignupRequest(null, "password123");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void メールアドレスの形式が無効() {
        SignupRequest request = new SignupRequest("invalid-email", "password123");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("有効なメールアドレス"));
    }

    @Test
    void パスワードが空() {
        SignupRequest request = new SignupRequest("test@example.com", "");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void パスワードがnull() {
        SignupRequest request = new SignupRequest("test@example.com", null);
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void パスワードが8文字未満() {
        SignupRequest request = new SignupRequest("test@example.com", "pass123");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("8文字以上"));
    }

    @Test
    void パスワードが8文字ちょうど() {
        SignupRequest request = new SignupRequest("test@example.com", "pass1234");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }
}

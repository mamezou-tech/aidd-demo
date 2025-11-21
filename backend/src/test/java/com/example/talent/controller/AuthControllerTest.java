package com.example.talent.controller;

import com.example.talent.application.AuthService;
import com.example.talent.config.JwtUtil;
import com.example.talent.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldLoginSuccessfully() throws Exception {
        User user = new User("U001", "test@example.com", "hash", "Test User",
                LocalDateTime.now(), LocalDateTime.now());
        when(authService.authenticate("test@example.com", "password")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("test-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    @WithMockUser
    void shouldRejectInvalidCredentials() throws Exception {
        when(authService.authenticate("test@example.com", "wrong")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }
}

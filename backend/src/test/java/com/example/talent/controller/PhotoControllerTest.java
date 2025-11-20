package com.example.talent.controller;

import com.example.talent.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldGetEmployeePhoto() throws Exception {
        mockMvc.perform(get("/api/employees/E001/photo"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnDefaultPhotoWhenNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/E999/photo"))
                .andExpect(status().isOk());
    }
}

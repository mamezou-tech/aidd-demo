package com.example.talent.controller;

import com.example.talent.application.OrganizationService;
import com.example.talent.config.JwtUtil;
import com.example.talent.domain.model.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldGetAllOrganizations() throws Exception {
        Organization org = new Organization("ORG001", "経営企画部", LocalDateTime.now(), LocalDateTime.now());
        when(organizationService.findAll()).thenReturn(List.of(org));

        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationId").value("ORG001"))
                .andExpect(jsonPath("$[0].organizationName").value("経営企画部"));
    }
}

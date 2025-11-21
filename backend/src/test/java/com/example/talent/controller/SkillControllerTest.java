package com.example.talent.controller;

import com.example.talent.application.SkillService;
import com.example.talent.config.JwtUtil;
import com.example.talent.domain.model.Skill;
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

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldGetAllSkills() throws Exception {
        Skill skill = new Skill("SK001", "Java", "プログラミング言語", LocalDateTime.now(), LocalDateTime.now());
        when(skillService.findAll()).thenReturn(List.of(skill));

        mockMvc.perform(get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skillId").value("SK001"))
                .andExpect(jsonPath("$[0].skillName").value("Java"));
    }
}


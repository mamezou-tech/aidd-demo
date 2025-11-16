package com.example.talent.controller;

import com.example.talent.application.EmployeeSearchService;
import com.example.talent.domain.Employee;
import com.example.talent.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@ContextConfiguration(classes = {EmployeeController.class, EmployeeControllerTest.TestSecurityConfig.class})
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeSearchService employeeSearchService;

    @MockBean
    private com.example.talent.application.EmployeeDetailService employeeDetailService;

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void 全社員を取得できる() throws Exception {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        Page<Employee> page = new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);
        when(employeeSearchService.search(isNull(), isNull(), isNull(), isNull(), 
                isNull(), isNull(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeCode").value("E001"))
                .andExpect(jsonPath("$.content[0].fullName").value("田中 太郎"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void 氏名で検索できる() throws Exception {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        Page<Employee> page = new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);
        when(employeeSearchService.search(eq("田中"), isNull(), isNull(), isNull(), 
                isNull(), isNull(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/employees?fullName=田中"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("田中 太郎"));
    }

    @Test
    void ページネーションが機能する() throws Exception {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        Page<Employee> page = new PageImpl<>(List.of(employee), PageRequest.of(1, 10), 25);
        when(employeeSearchService.search(isNull(), isNull(), isNull(), isNull(), 
                isNull(), isNull(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/employees?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(25));
    }

    @Test
    void 複数条件で検索できる() throws Exception {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.now());
        Page<Employee> page = new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);
        when(employeeSearchService.search(eq("田中"), isNull(), isNull(), isNull(), 
                eq("課長"), eq("正社員"), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/employees?fullName=田中&position=課長&employmentType=正社員"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].position").value("課長"))
                .andExpect(jsonPath("$.content[0].employmentType").value("正社員"));
    }

    @Test
    void IDで社員詳細を取得できる() throws Exception {
        // Given
        Employee employee = new Employee("E001", "田中 太郎", "ﾀﾅｶ ﾀﾛｳ",
                "tanaka@example.com", "課長", "正社員", LocalDate.of(2020, 4, 1));
        when(employeeDetailService.findById(1L)).thenReturn(employee);

        // When & Then
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("E001"))
                .andExpect(jsonPath("$.fullName").value("田中 太郎"))
                .andExpect(jsonPath("$.position").value("課長"));
    }

    @Test
    void 存在しない社員IDで404エラー() throws Exception {
        // Given
        when(employeeDetailService.findById(999L))
                .thenThrow(new RuntimeException("社員が見つかりません。ID: 999"));

        // When & Then
        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("社員が見つかりません。ID: 999"));
    }
}

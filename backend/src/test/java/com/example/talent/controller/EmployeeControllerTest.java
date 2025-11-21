package com.example.talent.controller;

import com.example.talent.application.EmployeeDetailDTO;
import com.example.talent.application.EmployeeDetailService;
import com.example.talent.application.EmployeeSearchService;
import com.example.talent.config.JwtUtil;
import com.example.talent.domain.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeSearchService employeeSearchService;

    @MockBean
    private EmployeeDetailService employeeDetailService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldGetAllEmployees() throws Exception {
        Employee employee = createEmployee("E001", "山田太郎", "ORG001");
        when(employeeSearchService.search(any())).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeId").value("E001"))
                .andExpect(jsonPath("$.content[0].name").value("山田太郎"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void shouldGetEmployeeById() throws Exception {
        EmployeeDetailDTO dto = new EmployeeDetailDTO(
            "E001", "山田太郎", "ORG001", "Engineer", "FULL_TIME",
            LocalDate.of(2020, 1, 1), "test@example.com", "/photos/E001.jpg",
            List.of()
        );
        when(employeeDetailService.getDetail("E001")).thenReturn(dto);

        mockMvc.perform(get("/api/employees/E001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("E001"))
                .andExpect(jsonPath("$.name").value("山田太郎"));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {
        when(employeeDetailService.getDetail("E999")).thenThrow(new IllegalArgumentException("Employee not found"));

        mockMvc.perform(get("/api/employees/E999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldSearchEmployeesByName() throws Exception {
        Employee employee = createEmployee("E001", "山田太郎", "ORG001");
        when(employeeSearchService.searchByName("太郎")).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/employees/search").param("name", "太郎"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("山田太郎"));
    }

    private Employee createEmployee(String id, String name, String orgId) {
        return new Employee(id, name, orgId, "エンジニア", "正社員",
                LocalDate.of(2020, 4, 1), "test@example.com", "/photos/" + id + ".jpg",
                false, LocalDateTime.now(), LocalDateTime.now());
    }
}

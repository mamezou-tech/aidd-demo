package com.example.talent.exception;

import com.example.talent.controller.EmployeeController;
import com.example.talent.application.EmployeeDetailService;
import com.example.talent.application.EmployeeSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeSearchService employeeSearchService;

    @MockBean
    private EmployeeDetailService employeeDetailService;

    @Test
    @WithMockUser
    void RuntimeExceptionで404エラーが返される() throws Exception {
        when(employeeDetailService.findById(anyLong()))
                .thenThrow(new RuntimeException("社員が見つかりません。ID: 999"));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("社員が見つかりません。ID: 999"));
    }

    @Test
    @WithMockUser
    void その他のRuntimeExceptionで500エラーが返される() throws Exception {
        when(employeeDetailService.findById(anyLong()))
                .thenThrow(new RuntimeException("データベース接続エラー"));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("システムエラーが発生しました。"));
    }
}

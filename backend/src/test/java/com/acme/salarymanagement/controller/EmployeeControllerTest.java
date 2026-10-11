package com.acme.salarymanagement.controller;
import org.springframework.context.annotation.Import;
import com.acme.salarymanagement.dto.EmployeePageResponse;
import com.acme.salarymanagement.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(com.acme.salarymanagement.service.EmployeeService.class)
class EmployeeControllerTest {
    @Autowired MockMvc mvc;
    @MockBean EmployeeService service;

    @Test void healthEndpointReturnsOk() throws Exception {
        mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));
    }

    @Test void employeeListReturnsPaginationMetadata() throws Exception {
        when(service.list(0, 20, null, null, null)).thenReturn(new EmployeePageResponse(List.of(), 0, 0, 20, 0));
        mvc.perform(get("/api/employees")).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(0)).andExpect(jsonPath("$.page").value(0));
    }
}

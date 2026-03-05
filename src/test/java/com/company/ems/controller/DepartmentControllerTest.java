package com.company.ems.controller;


import com.company.ems.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_can_get_departments() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_cannot_create_department() throws Exception {
        mockMvc.perform(
                post("/api/departments")
                        .contentType("application/json")
                        .content("{}")
        ).andExpect(status().isForbidden());
    }
}


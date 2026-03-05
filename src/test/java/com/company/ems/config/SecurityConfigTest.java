package com.company.ems.config;

import com.company.ems.controller.EmployeeController;
import com.company.ems.controller.LeaveRequestController;
import com.company.ems.controller.DepartmentController;
import com.company.ems.service.EmployeeService;
import com.company.ems.service.LeaveRequestService;
import com.company.ems.service.DepartmentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {
                EmployeeController.class,
                LeaveRequestController.class,
                DepartmentController.class
        }
)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private LeaveRequestService leaveRequestService;

    @MockitoBean
    private DepartmentService departmentService;


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_can_access_employees() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void user_can_apply_leave() throws Exception {

        mockMvc.perform(post("/api/leaves")
                        .contentType("application/json")
                        .content("""
                                {
                                  "employeeId": 1,
                                  "startDate": "2026-02-01",
                                  "endDate": "2026-02-03",
                                  "reason": "Medical"
                                }
                                """))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void user_cannot_access_admin_endpoints() throws Exception {

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticated_user_blocked() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }
}

package com.company.ems.controller;


import com.company.ems.service.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveRequestController.class)
class LeaveRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveRequestService leaveRequestService;


    @Test
    @WithMockUser(roles = "USER")
    void user_cannot_update_leave_status() throws Exception {
        mockMvc.perform(
                put("/api/leaves/1/status")
                        .param("status", "APPROVED")
        ).andExpect(status().isForbidden());
    }
}


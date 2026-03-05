package com.company.ems.service;

import com.company.ems.dto.request.LeaveRequestCreateRequest;
import com.company.ems.dto.response.LeaveRequestResponse;

import java.util.List;

public interface LeaveRequestService {

    LeaveRequestResponse applyLeave(LeaveRequestCreateRequest request);

    LeaveRequestResponse updateLeaveStatus(Long leaveId, String status);

    List<LeaveRequestResponse> getLeavesByEmployee(Long employeeId);
}

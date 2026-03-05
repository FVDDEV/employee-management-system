package com.company.ems.mapper;

import com.company.ems.dto.response.LeaveRequestResponse;
import com.company.ems.entity.LeaveRequest;

public final class LeaveRequestMapper {

    private LeaveRequestMapper() {}

    public static LeaveRequestResponse toResponse(
            LeaveRequest leave) {

        LeaveRequestResponse dto =
                new LeaveRequestResponse();

        dto.setLeaveId(leave.getLeaveId());
        dto.setEmployeeId(
                leave.getEmployee().getEmployeeId());
        dto.setEmployeeName(
                leave.getEmployee().getFullName());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason());
        dto.setCreatedAt(leave.getCreatedAt());

        return dto;
    }
}

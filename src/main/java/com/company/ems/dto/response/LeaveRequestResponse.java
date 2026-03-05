package com.company.ems.dto.response;

import com.company.ems.enums.LeaveStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class LeaveRequestResponse {

    private Long leaveId;

    private Long employeeId;
    private String employeeName;

    private LocalDate startDate;
    private LocalDate endDate;

    private LeaveStatus status;
    private String reason;

    private LocalDateTime createdAt;
}

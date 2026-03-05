package com.company.ems.dto.event;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class NotificationEvent implements Serializable {

    private String eventType;
    private String email;
    private String message;

    private Long employeeId;
    private String employeeName;
    private String departmentName;

    private Long leaveId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveStatus;
}

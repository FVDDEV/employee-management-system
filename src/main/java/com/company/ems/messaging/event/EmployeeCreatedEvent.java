package com.company.ems.messaging.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeCreatedEvent {

    private Long employeeId;
    private String fullName;
    private String email;
    private String departmentName;
    private LocalDateTime createdAt;
}

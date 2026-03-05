package com.company.ems.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeResponse {

    private Long employeeId;
    private String fullName;
    private String email;

    private Long departmentId;
    private String departmentName;

    private BigDecimal salary;
    private LocalDate joiningDate;
    private LocalDateTime createdAt;
}

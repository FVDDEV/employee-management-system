package com.company.ems.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepartmentResponseDto {

    private Long departmentId;
    private String departmentName;
    private String location;
    private LocalDateTime createdAt;
}

package com.company.ems.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDto {

    @NotBlank(message = "Department name is required")
    private String departmentName;

    private String location;
}

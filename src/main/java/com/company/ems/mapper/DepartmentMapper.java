package com.company.ems.mapper;

import com.company.ems.dto.request.DepartmentRequestDto;
import com.company.ems.dto.response.DepartmentResponseDto;
import com.company.ems.entity.Department;

public final class DepartmentMapper {

    private DepartmentMapper() {}

    public static Department toEntity(DepartmentRequestDto dto) {
        Department department = new Department();
        department.setDepartmentName(dto.getDepartmentName());
        department.setLocation(dto.getLocation());
        return department;
    }

    public static DepartmentResponseDto toDto(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setLocation(department.getLocation());
        dto.setCreatedAt(department.getCreatedAt());
        return dto;
    }
}

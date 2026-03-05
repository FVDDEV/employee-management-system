package com.company.ems.mapper;

import com.company.ems.dto.request.EmployeeCreateRequest;
import com.company.ems.dto.request.EmployeeUpdateRequest;
import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toEntity(
            EmployeeCreateRequest request,
            Department department) {

        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartment(department);
        return employee;
    }

    public static void updateEntity(
            Employee employee,
            EmployeeUpdateRequest request,
            Department department) {

        employee.setFullName(request.getFullName());
        employee.setSalary(request.getSalary());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartment(department);
    }

    public static EmployeeResponse toResponse(Employee employee) {

        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setFullName(employee.getFullName());
        response.setEmail(employee.getEmail());
        response.setSalary(employee.getSalary());
        response.setJoiningDate(employee.getJoiningDate());
        response.setCreatedAt(employee.getCreatedAt());

        response.setDepartmentId(
                employee.getDepartment().getDepartmentId());
        response.setDepartmentName(
                employee.getDepartment().getDepartmentName());

        return response;
    }
}

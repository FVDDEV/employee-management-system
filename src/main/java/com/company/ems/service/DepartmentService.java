package com.company.ems.service;

import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Department getById(Long departmentId);

    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);

}

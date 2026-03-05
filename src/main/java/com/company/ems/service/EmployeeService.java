package com.company.ems.service;

import com.company.ems.dto.request.EmployeeCreateRequest;
import com.company.ems.dto.request.EmployeeUpdateRequest;
import com.company.ems.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request);

    void deleteEmployee(Long employeeId);

    EmployeeResponse getEmployeeById(Long employeeId);

    Page<EmployeeResponse> getEmployees(
            Integer page,
            Integer size,
            String sortBy,
            String direction,
            Long departmentId
    );

    EmployeeResponse getMyProfile();
}

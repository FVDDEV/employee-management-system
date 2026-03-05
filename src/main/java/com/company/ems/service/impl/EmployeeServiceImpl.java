package com.company.ems.service.impl;

import com.company.ems.dto.request.EmployeeCreateRequest;
import com.company.ems.dto.request.EmployeeUpdateRequest;
import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import com.company.ems.entity.LeaveRequest;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.messaging.producer.EmployeeEventProducer;
import com.company.ems.repository.DepartmentRepository;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.repository.LeaveRequestRepository;
import com.company.ems.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeEventProducer employeeEventProducer;
    private final LeaveRequestRepository leaveRequestRepository;


    @Override
    public EmployeeResponse createEmployee(
            EmployeeCreateRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"));

        Employee employee =
                EmployeeMapper.toEntity(request, department);

        Employee savedEmployee =
                employeeRepository.save(employee);

        employeeEventProducer
                .publishEmployeeCreated(savedEmployee);

        return EmployeeMapper.toResponse(savedEmployee);
    }


    @Override
    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"));

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"));

        EmployeeMapper.updateEntity(
                employee, request, department);

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return EmployeeMapper.toResponse(updatedEmployee);
    }


    @Override
    public void deleteEmployee(Long employeeId) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"));

        List<LeaveRequest> leaves =
                leaveRequestRepository.findByEmployee(employee);

        if (!leaves.isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete employee. Leave requests exist for this employee.");
        }
        employeeRepository.delete(employee);
    }


    @Override
    public EmployeeResponse getEmployeeById(
            Long employeeId) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"));

        return EmployeeMapper.toResponse(employee);
    }

    @Override
    public Page<EmployeeResponse> getEmployees(
            Integer page,
            Integer size,
            String sortBy,
            String direction,
            Long departmentId) {

        Sort sort =
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Employee> employeePage =
                (departmentId != null)
                        ? employeeRepository
                        .findByDepartment_DepartmentId(
                                departmentId, pageable)
                        : employeeRepository.findAll(pageable);

        return employeePage.map(
                EmployeeMapper::toResponse);
    }

    @Override
    public EmployeeResponse getMyProfile() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Employee employee = employeeRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return EmployeeMapper.toResponse(employee);
    }

}

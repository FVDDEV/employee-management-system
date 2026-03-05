package com.company.ems.service.impl;

import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.repository.DepartmentRepository;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public Department createDepartment(final Department department) {

        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new BadRequestException("Department already exists");
        }

        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + departmentId));
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found"));

        List<Employee> employees =
                employeeRepository.findByDepartment(department);

        return employees.stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }
}

package com.company.ems.service;


import com.company.ems.dto.request.EmployeeCreateRequest;
import com.company.ems.dto.request.EmployeeUpdateRequest;
import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import com.company.ems.entity.LeaveRequest;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.messaging.producer.EmployeeEventProducer;
import com.company.ems.repository.DepartmentRepository;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.repository.LeaveRequestRepository;
import com.company.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeEventProducer employeeEventProducer;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;
    private Employee employee;

    @BeforeEach
    void setup() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("IT");

        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFullName("Bhavesh Zala");
        employee.setEmail("bhavesh@test.com");
        employee.setSalary(BigDecimal.valueOf(50000));
        employee.setJoiningDate(LocalDate.now());
        employee.setDepartment(department);
    }

    @Test
    void createEmployee_success() {

        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setFullName("Bhavesh Zala");
        request.setEmail("bhavesh@test.com");
        request.setSalary(BigDecimal.valueOf(50000));
        request.setJoiningDate(LocalDate.now());
        request.setDepartmentId(1L);

        when(employeeRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> {
                    Employee emp = invocation.getArgument(0);
                    emp.setEmployeeId(1L);
                    return emp;
                });

        EmployeeResponse response =
                employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals("Bhavesh Zala", response.getFullName());

        verify(employeeEventProducer, times(1))
                .publishEmployeeCreated(any(Employee.class));
    }

    @Test
    void createEmployee_emailAlreadyExists() {

        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setEmail("bhavesh@test.com");

        when(employeeRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> employeeService.createEmployee(request));

        verify(employeeEventProducer, never())
                .publishEmployeeCreated(any());
    }


    @Test
    void createEmployee_departmentNotFound() {

        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setEmail("bhavesh@test.com");
        request.setDepartmentId(99L);

        when(employeeRepository.existsByEmail(any()))
                .thenReturn(false);

        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.createEmployee(request));
    }

    @Test
    void updateEmployee_success() {

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();
        request.setFullName("Updated Name");
        request.setSalary(BigDecimal.valueOf(70000));
        request.setDepartmentId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(i -> i.getArgument(0));

        EmployeeResponse response =
                employeeService.updateEmployee(1L, request);

        assertEquals("Updated Name", response.getFullName());
    }


    @Test
    void updateEmployee_notFound() {

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(1L, request));
    }


    @Test
    void deleteEmployee_success() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRequestRepository.findByEmployee(employee))
                .thenReturn(List.of());

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1))
                .delete(employee);
    }

    @Test
    void deleteEmployee_shouldFail_whenLeavesExist() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRequestRepository.findByEmployee(employee))
                .thenReturn(List.of(new LeaveRequest()));

        BadRequestException ex =
                assertThrows(BadRequestException.class,
                        () -> employeeService.deleteEmployee(1L));

        assertEquals(
                "Cannot delete employee. Leave requests exist for this employee.",
                ex.getMessage()
        );

        verify(employeeRepository, never()).delete(any());
    }


    @Test
    void deleteEmployee_notFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));
    }


    @Test
    void getEmployeeById_success() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response =
                employeeService.getEmployeeById(1L);

        assertEquals("Bhavesh Zala", response.getFullName());
    }


    @Test
    void getEmployees_success() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page =
                new PageImpl<>(List.of(employee), pageable, 1);

        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<EmployeeResponse> response =
                employeeService.getEmployees(
                        0, 10, "fullName", "asc", null);

        assertEquals(1, response.getTotalElements());
    }
}

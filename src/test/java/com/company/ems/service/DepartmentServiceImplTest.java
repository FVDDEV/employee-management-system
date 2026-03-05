package com.company.ems.service;

import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.repository.DepartmentRepository;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private Employee employee;

    @BeforeEach
    void setup() {

        department = new Department();
        department.setDepartmentId(1L);
        department.setDepartmentName("IT");

        employee = new Employee();
        employee.setEmployeeId(10L);
        employee.setFullName("Bhavesh Zala");
        employee.setEmail("bhavesh@test.com");
        employee.setDepartment(department);
    }


    @Test
    void createDepartment_success() {

        when(departmentRepository.existsByDepartmentName("IT"))
                .thenReturn(false);

        when(departmentRepository.save(department))
                .thenReturn(department);

        Department saved =
                departmentService.createDepartment(department);

        assertNotNull(saved);
        assertEquals("IT", saved.getDepartmentName());

        verify(departmentRepository).save(department);
    }

    @Test
    void createDepartment_duplicateDepartment() {

        when(departmentRepository.existsByDepartmentName("IT"))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> departmentService.createDepartment(department));

        verify(departmentRepository, never()).save(any());
    }


    @Test
    void getAllDepartments_success() {

        when(departmentRepository.findAll())
                .thenReturn(List.of(department));

        List<Department> result =
                departmentService.getAllDepartments();

        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getDepartmentName());
    }


    @Test
    void getById_success() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        Department result =
                departmentService.getById(1L);

        assertEquals("IT", result.getDepartmentName());
    }


    @Test
    void getById_notFound() {

        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getById(99L));
    }


    @Test
    void getEmployeesByDepartment_success() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.findByDepartment(department))
                .thenReturn(List.of(employee));

        List<EmployeeResponse> responses =
                departmentService.getEmployeesByDepartment(1L);

        assertEquals(1, responses.size());
        assertEquals("Bhavesh Zala",
                responses.get(0).getFullName());
        assertEquals("IT",
                responses.get(0).getDepartmentName());
    }

    @Test
    void getEmployeesByDepartment_departmentNotFound() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getEmployeesByDepartment(1L));
    }
}

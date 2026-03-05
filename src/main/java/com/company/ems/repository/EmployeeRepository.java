package com.company.ems.repository;

import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment_DepartmentId(
            Long departmentId,
            Pageable pageable
    );

    boolean existsByEmail(String email);

    List<Employee> findByDepartment(Department department);

    Optional<Employee> findByEmail(String email);


}

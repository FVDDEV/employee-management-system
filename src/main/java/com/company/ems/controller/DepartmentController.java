package com.company.ems.controller;

import com.company.ems.dto.request.DepartmentRequestDto;
import com.company.ems.dto.response.DepartmentResponseDto;
import com.company.ems.dto.response.EmployeeResponse;
import com.company.ems.entity.Department;
import com.company.ems.mapper.DepartmentMapper;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody DepartmentRequestDto request) {

        Department department =
                DepartmentMapper.toEntity(request);

        Department saved =
                departmentService.createDepartment(department);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DepartmentMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {

        List<DepartmentResponseDto> response =
                departmentService.getAllDepartments()
                        .stream()
                        .map(DepartmentMapper::toDto)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeResponse>>
    getDepartmentEmployees(@PathVariable Long id) {

        List<EmployeeResponse> response =
                departmentService.getEmployeesByDepartment(id);

        return ResponseEntity.ok(response);
    }

}

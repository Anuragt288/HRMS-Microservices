package com.hrms.department.controller;

import com.hrms.department.dto.ApiResponse;
import com.hrms.department.dto.DepartmentRequest;
import com.hrms.department.entity.Department;
import com.hrms.department.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Department>> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        Department department =
                departmentService.createDepartment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Department created successfully",
                        department
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {

        List<Department> departments =
                departmentService.getAllDepartments();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Departments retrieved successfully",
                        departments
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(
            @PathVariable UUID id) {

        Department department =
                departmentService.getDepartmentById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Department retrieved successfully",
                        department
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {

        Department department =
                departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Department updated successfully",
                        department
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDepartment(
            @PathVariable UUID id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Department deleted successfully",
                        null
                )
        );
    }
}
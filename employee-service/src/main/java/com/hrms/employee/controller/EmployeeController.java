package com.hrms.employee.controller;

import com.hrms.employee.dto.EmployeeRequest;
import com.hrms.employee.dto.EmployeeResponse;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import com.hrms.employee.dto.ApiResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create employee
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        Employee createdEmployee =
                employeeService.createEmployee(employeeRequest);

        EmployeeResponse employeeResponse =
                employeeService.mapToResponse(createdEmployee);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                HttpStatus.CREATED.value(),
                                "Employee created successfully",
                                employeeResponse
                        )
                );
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getAllEmployees(
            @PageableDefault(size = 10, sort = "firstName")
            Pageable pageable) {

        Page<EmployeeResponse> employees =
                employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employees retrieved successfully",
                        employees
                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> searchEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "firstName")
            Pageable pageable) {

        Page<EmployeeResponse> employees =
                employeeService.searchEmployees(
                        department,
                        status,
                        search,
                        pageable
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employees retrieved successfully",
                        employees
                )
        );
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(
            @PathVariable UUID id) {

        Employee employee =
                employeeService.getEmployeeById(id);

        EmployeeResponse employeeResponse =
                employeeService.mapToResponse(employee);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employee retrieved successfully",
                        employeeResponse
                )
        );
    }

    // Update employee
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        Employee updatedEmployee =
                employeeService.updateEmployee(id, employeeRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employee updated successfully",
                        updatedEmployee
                )
        );
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(
            @PathVariable UUID id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employee deleted successfully",
                        null
                )
        );
    }
}
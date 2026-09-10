package com.hrms.employee.controller;

import com.hrms.employee.dto.EmployeeRequest;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        Employee createdEmployee =
                employeeService.createEmployee(employeeRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @PageableDefault(size = 10, sort = "firstName")
            Pageable pageable) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(pageable)
        );
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }

    // Update employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employeeRequest)
        );
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable UUID id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                "Employee deleted successfully"
        );
    }
}
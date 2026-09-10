package com.hrms.employee.service;

import com.hrms.employee.dto.EmployeeRequest;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(EmployeeRequest request) {

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        return employeeRepository.save(employee);
    }

    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);

    }
    public List<Employee> searchEmployees(
            String department,
            String status,
            String search) {

        if (department != null && !department.isBlank()) {
            return employeeRepository.findByDepartmentIgnoreCase(department);
        }

        if (status != null && !status.isBlank()) {
            return employeeRepository.findByStatusIgnoreCase(status);
        }

        if (search != null && !search.isBlank()) {
            return employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            search, search);
        }

        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
    }

    public Employee updateEmployee(UUID id, EmployeeRequest request) {

        Employee employee = getEmployeeById(id);

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {

        Employee employee = getEmployeeById(id);

        employeeRepository.delete(employee);
    }
}
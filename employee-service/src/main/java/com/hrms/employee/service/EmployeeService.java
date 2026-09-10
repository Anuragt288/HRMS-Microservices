package com.hrms.employee.service;

import com.hrms.employee.dto.EmployeeRequest;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import com.hrms.employee.exception.DuplicateEmployeeException;
import org.springframework.data.jpa.domain.Specification;
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

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateEmployeeException("Employee code already exists");
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("Email already exists");        }

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
    public Page<Employee> searchEmployees(
            String department,
            String status,
            String search,
            Pageable pageable) {

        Specification<Employee> specification = Specification.where(null);

        if (department != null && !department.isBlank()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.lower(root.get("department")),
                                    department.toLowerCase()
                            )
            );
        }

        if (status != null && !status.isBlank()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.lower(root.get("status")),
                                    status.toLowerCase()
                            )
            );
        }

        if (search != null && !search.isBlank()) {
            String searchPattern = "%" + search.toLowerCase() + "%";

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.or(
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(root.get("firstName")),
                                            searchPattern
                                    ),
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(root.get("lastName")),
                                            searchPattern
                                    )
                            )
            );
        }

        return employeeRepository.findAll(specification, pageable);
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
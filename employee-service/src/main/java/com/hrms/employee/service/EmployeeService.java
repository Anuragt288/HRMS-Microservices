package com.hrms.employee.service;

import com.hrms.employee.client.DepartmentClient;
import com.hrms.employee.dto.ApiResponse;
import com.hrms.employee.dto.DepartmentResponse;
import com.hrms.employee.dto.EmployeeRequest;
import com.hrms.employee.dto.EmployeeResponse;
import com.hrms.employee.entity.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import com.hrms.employee.exception.DuplicateEmployeeException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import feign.FeignException;
import com.hrms.employee.exception.DepartmentNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentClient departmentClient;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentClient departmentClient) {
        this.employeeRepository = employeeRepository;
        this.departmentClient = departmentClient;
    }

    public Employee createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateEmployeeException("Employee code already exists");
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("Email already exists");        }

        if (request.getDepartmentId() != null) {
            try {
                departmentClient.getDepartmentById(request.getDepartmentId());
            } catch (FeignException.BadRequest exception) {
                throw new DepartmentNotFoundException("Department not found");
            } catch (FeignException.NotFound exception) {
                throw new DepartmentNotFoundException("Department not found");
            }
        }

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        return employeeRepository.save(employee);
    }

    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {

        Page<Employee> employees = employeeRepository.findAll(pageable);

        return employees.map(this::mapToResponse);
    }
    public Page<EmployeeResponse> searchEmployees(
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

        Page<Employee> employees =
                employeeRepository.findAll(specification, pageable);

        return employees.map(this::mapToResponse);
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
    }

    public Employee updateEmployee(UUID id, EmployeeRequest request) {

        Employee employee = getEmployeeById(id);

        if (request.getDepartmentId() != null) {
            try {
                departmentClient.getDepartmentById(request.getDepartmentId());
            } catch (FeignException.BadRequest | FeignException.NotFound exception) {
                throw new DepartmentNotFoundException("Department not found");
            }
        }

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {

        Employee employee = getEmployeeById(id);

        employeeRepository.delete(employee);
    }

    public EmployeeResponse mapToResponse(Employee employee) {

        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());

        response.setDepartmentId(employee.getDepartmentId());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setJoiningDate(employee.getJoiningDate());
        response.setStatus(employee.getStatus());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());

        if (employee.getDepartmentId() != null) {
            try {
                ApiResponse<DepartmentResponse> departmentResponse =
                        departmentClient.getDepartmentById(employee.getDepartmentId());

                if (departmentResponse != null && departmentResponse.getData() != null) {
                    response.setDepartmentName(
                            departmentResponse.getData().getName()
                    );
                }

            } catch (FeignException.BadRequest | FeignException.NotFound exception) {
                response.setDepartmentName(null);
            }
        }

        return response;
    }
}
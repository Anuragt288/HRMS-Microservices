package com.hrms.department.service;

import com.hrms.department.dto.DepartmentRequest;
import com.hrms.department.entity.Department;
import com.hrms.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(DepartmentRequest request) {

        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        Department department = new Department();

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setStatus(request.getStatus());

        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(UUID id) {

        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));
    }

    public Department updateDepartment(
            UUID id,
            DepartmentRequest request) {

        Department department = getDepartmentById(id);

        if (!department.getName().equalsIgnoreCase(request.getName())
                && departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setStatus(request.getStatus());

        return departmentRepository.save(department);
    }

    public void deleteDepartment(UUID id) {

        Department department = getDepartmentById(id);

        departmentRepository.delete(department);
    }
}
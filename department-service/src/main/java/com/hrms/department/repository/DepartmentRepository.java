package com.hrms.department.repository;

import com.hrms.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
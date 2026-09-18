package com.hrms.employee.client;

import com.hrms.employee.config.FeignConfig;
import com.hrms.employee.dto.ApiResponse;
import com.hrms.employee.dto.DepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "DEPARTMENT-SERVICE",
        configuration = FeignConfig.class
)
public interface DepartmentClient {

    @GetMapping("/api/departments/{id}")
    ApiResponse<DepartmentResponse> getDepartmentById(
            @PathVariable("id") UUID id);
}
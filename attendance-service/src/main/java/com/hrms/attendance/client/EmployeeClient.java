package com.hrms.attendance.client;

import com.hrms.attendance.config.FeignConfig;
import com.hrms.attendance.dto.ApiResponse;
import com.hrms.attendance.dto.EmployeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "EMPLOYEE-SERVICE",
        configuration = FeignConfig.class
)
public interface EmployeeClient {

    @GetMapping("/api/employees/{id}")
    ApiResponse<EmployeeResponse> getEmployeeById(
            @PathVariable("id") UUID id
    );
}
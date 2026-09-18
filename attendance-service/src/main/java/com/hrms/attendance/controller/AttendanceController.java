package com.hrms.attendance.controller;

import com.hrms.attendance.dto.ApiResponse;
import com.hrms.attendance.dto.AttendanceRequest;
import com.hrms.attendance.entity.Attendance;
import com.hrms.attendance.service.AttendanceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Attendance>> createAttendance(
            @Valid @RequestBody AttendanceRequest request) {

        Attendance attendance =
                attendanceService.createAttendance(request);

        ApiResponse<Attendance> response =
                new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Attendance created successfully",
                        attendance
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Attendance>>> getAllAttendance() {

        List<Attendance> attendance =
                attendanceService.getAllAttendance();

        ApiResponse<List<Attendance>> response =
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Attendance fetched successfully",
                        attendance
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Attendance>> getAttendanceById(
            @PathVariable UUID id) {

        Attendance attendance =
                attendanceService.getAttendanceById(id);

        ApiResponse<Attendance> response =
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Attendance fetched successfully",
                        attendance
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<Attendance>>>
    getAttendanceByEmployee(
            @PathVariable UUID employeeId) {

        List<Attendance> attendance =
                attendanceService.getAttendanceByEmployee(employeeId);

        ApiResponse<List<Attendance>> response =
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employee attendance fetched successfully",
                        attendance
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Attendance>> updateAttendance(
            @PathVariable UUID id,
            @Valid @RequestBody AttendanceRequest request) {

        Attendance attendance =
                attendanceService.updateAttendance(id, request);

        ApiResponse<Attendance> response =
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Attendance updated successfully",
                        attendance
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(
            @PathVariable UUID id) {

        attendanceService.deleteAttendance(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Attendance deleted successfully",
                        null
                );

        return ResponseEntity.ok(response);
    }
}
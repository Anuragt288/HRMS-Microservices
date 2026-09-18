package com.hrms.attendance.service;

import com.hrms.attendance.dto.AttendanceRequest;
import com.hrms.attendance.entity.Attendance;
import com.hrms.attendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import com.hrms.attendance.client.EmployeeClient;
import com.hrms.attendance.dto.ApiResponse;
import com.hrms.attendance.dto.EmployeeResponse;
import feign.FeignException;

import com.hrms.attendance.exception.EmployeeNotFoundException;
import com.hrms.attendance.exception.AttendanceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final EmployeeClient employeeClient;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeClient employeeClient) {

        this.attendanceRepository = attendanceRepository;
        this.employeeClient = employeeClient;
    }

    public Attendance createAttendance(AttendanceRequest request) {

        try {

            ApiResponse<EmployeeResponse> employeeResponse =
                    employeeClient.getEmployeeById(request.getEmployeeId());

            if (employeeResponse == null ||
                    employeeResponse.getData() == null) {

                throw new EmployeeNotFoundException("Employee not found");            }

        } catch (FeignException.NotFound |
                 FeignException.BadRequest exception) {

            throw new EmployeeNotFoundException("Employee not found");        }

        validateStatus(request.getStatus());

        if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(
                request.getEmployeeId(),
                request.getAttendanceDate())) {

            throw new RuntimeException(
                    "Attendance already exists for this employee on this date"
            );
        }

        Attendance attendance = new Attendance();

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(UUID id) {

        return attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found"));
    }

    public List<Attendance> getAttendanceByEmployee(UUID employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }

    public Attendance updateAttendance(
            UUID id,
            AttendanceRequest request) {

        try {

            ApiResponse<EmployeeResponse> employeeResponse =
                    employeeClient.getEmployeeById(request.getEmployeeId());

            if (employeeResponse == null ||
                    employeeResponse.getData() == null) {

                throw new RuntimeException("Employee not found");
            }

        } catch (FeignException.NotFound |
                 FeignException.BadRequest exception) {

            throw new RuntimeException("Employee not found");
        }

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found"));

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(UUID id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }

    private void validateStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new RuntimeException("Attendance status is required");
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!normalizedStatus.equals("PRESENT")
                && !normalizedStatus.equals("ABSENT")
                && !normalizedStatus.equals("HALF_DAY")
                && !normalizedStatus.equals("LEAVE")) {

            throw new RuntimeException(
                    "Invalid attendance status. Allowed values: PRESENT, ABSENT, HALF_DAY, LEAVE"
            );
        }
    }
}
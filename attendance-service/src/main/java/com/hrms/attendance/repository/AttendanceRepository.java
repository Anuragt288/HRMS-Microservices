package com.hrms.attendance.repository;

import com.hrms.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    boolean existsByEmployeeIdAndAttendanceDate(
            UUID employeeId,
            LocalDate attendanceDate
    );

    List<Attendance> findByEmployeeId(UUID employeeId);

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(
            UUID employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
}
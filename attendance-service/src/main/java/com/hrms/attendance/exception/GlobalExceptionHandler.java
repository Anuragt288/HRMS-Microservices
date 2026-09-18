package com.hrms.attendance.exception;

import com.hrms.attendance.dto.ApiResponse;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>>
    handleValidationException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed",
                        errors
                );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleConstraintViolationException(
            ConstraintViolationException exception) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleEmployeeNotFoundException(
            EmployeeNotFoundException exception) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleAttendanceNotFoundException(
            AttendanceNotFoundException exception) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleRuntimeException(RuntimeException exception) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        null
                );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>>
    handleException(Exception exception) {

        ApiResponse<Void> response =
                new ApiResponse<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error",
                        null
                );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
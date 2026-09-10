package com.hrms.auth.controller;

import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.dto.LoginResponse;
import com.hrms.auth.service.AuthService;

import jakarta.validation.Valid;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrms.auth.dto.RefreshTokenRequest;
import com.hrms.auth.dto.RefreshTokenResponse;
import com.hrms.auth.dto.ApiResponse;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        System.out.println("LOGIN CONTROLLER HIT");

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Login successful",
                        response
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        RefreshTokenResponse response =
                authService.refreshAccessToken(
                        request.getRefreshToken()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Access token refreshed successfully",
                        response
                )
        );    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(Authentication authentication) {

        authService.logout(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Logout successful",
                        null
                )
        );
    }
}
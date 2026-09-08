package com.hrms.auth.dto;

import java.util.Set;

public class LoginResponse {

    private String message;
    private String userId;
    private String username;
    private String email;
    private Set<String> roles;
    private String token;

    private String refreshToken;

    public LoginResponse(
            String message,
            String userId,
            String username,
            String email,
            Set<String> roles,
            String token,
            String refreshToken
    ) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
package com.hrms.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {
        String path = exchange.getRequest()
                .getURI()
                .getPath();

        if (path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/employee-service/swagger-ui")
                || path.startsWith("/employee-service/v3/api-docs")
                || path.startsWith("/employee-service/webjars")) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // Token missing hai
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        // Token invalid ya expired hai
        if (!jwtService.isTokenValid(token)) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        // JWT se role extract karo
        String role = jwtService.extractRole(token);

        String method = exchange.getRequest()
                .getMethod()
                .name();

        // Employee APIs par role-based authorization
        if (path.startsWith("/api/employees")) {

            boolean isReadRequest = method.equals("GET");
            boolean isAdmin = "ADMIN".equals(role);

            // GET ADMIN aur EMPLOYEE dono kar sakte hain
            // POST, PUT, DELETE sirf ADMIN kar sakta hai
            if (!isReadRequest && !isAdmin) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);

                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }
}
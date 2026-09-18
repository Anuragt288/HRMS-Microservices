package com.hrms.department.config;

import com.hrms.department.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/",
                                "/error",
                                "/actuator/health"
                        ).permitAll()

                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/api/departments/**"
                        ).hasAnyRole("ADMIN", "EMPLOYEE")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/departments/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.PUT,
                                "/api/departments/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.DELETE,
                                "/api/departments/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) -> {

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");

                            response.getWriter().write("""
                    {
                        "status": 401,
                        "message": "Authentication token is required",
                        "data": null
                    }
                    """);
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {

                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");

                            response.getWriter().write("""
                    {
                        "status": 403,
                        "message": "Access denied",
                        "data": null
                    }
                    """);
                        })
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
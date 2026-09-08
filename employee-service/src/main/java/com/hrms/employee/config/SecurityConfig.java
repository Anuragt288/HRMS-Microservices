package com.hrms.employee.config;

import com.hrms.employee.security.JwtAuthenticationFilter;

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
                                        "/v3/api-docs/**","/",
                                        "/error",
                                        "/actuator/health"
                                ).permitAll()

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.GET,
                                        "/api/employees/**"
                                ).hasAnyRole("ADMIN", "EMPLOYEE")

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.POST,
                                        "/api/employees/**"
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.PUT,
                                        "/api/employees/**"
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        org.springframework.http.HttpMethod.DELETE,
                                        "/api/employees/**"
                                ).hasRole("ADMIN")

                                .anyRequest().authenticated()
                        )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
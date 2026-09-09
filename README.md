# HRMS Microservices Project

## Overview

This project is a Human Resource Management System built using Spring Boot and Microservices Architecture.

## Microservices

- Auth Service
- Employee Service
- API Gateway
- Service Registry
- MySQL Database

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Cloud Gateway
- Eureka Service Registry
- Spring Data JPA
- MySQL
- Docker
- Swagger/OpenAPI

## Service Ports

| Service | Port |
|---|---:|
| API Gateway | 8080 |
| Auth Service | 8081 |
| Employee Service | 8082 |
| Service Registry | 8761 |
| MySQL | 3307 |

## Features

- User login using JWT
- Refresh token
- Logout
- Role-based authorization
- Employee CRUD operations
- Admin and Employee roles
- API Gateway routing
- Eureka service discovery
- Swagger API documentation
- Docker Compose setup

## API Documentation

### Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | User login and JWT generation |
| POST | `/api/auth/refresh` | Generate a new access token |
| POST | `/api/auth/logout` | Invalidate refresh token |

### Employee APIs

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/employees` | ADMIN, EMPLOYEE |
| GET | `/api/employees/{id}` | ADMIN, EMPLOYEE |
| POST | `/api/employees` | ADMIN |
| PUT | `/api/employees/{id}` | ADMIN |
| DELETE | `/api/employees/{id}` | ADMIN |

### Swagger UI

Employee Service Swagger UI through API Gateway:

`http://localhost:8080/employee-service/swagger-ui/index.html`

## Docker Setup

### Prerequisites

- Docker
- Docker Compose

### Start All Services

From the project root directory:

```bash
docker compose up --build
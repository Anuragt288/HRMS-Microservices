# HRMS Microservices Project

## Overview

This project is a Human Resource Management System (HRMS) built using Spring Boot and Microservices Architecture.

The system is designed with independent services for authentication and employee management, with API Gateway and Eureka Service Registry handling routing and service discovery.

## Architecture

The project consists of the following services:

- Auth Service
- Employee Service
- API Gateway
- Eureka Service Registry
- MySQL Databases

### Request Flow

Client → API Gateway → Microservice → MySQL

The API Gateway routes requests to the appropriate microservice, while Eureka provides service discovery.

## Microservices

### Auth Service

Responsible for:

- User login
- JWT access token generation
- Refresh token management
- Logout
- Role-based authentication

### Employee Service

Responsible for:

- Employee creation
- Employee retrieval
- Employee update
- Employee deletion
- Request validation
- Role-based authorization

### API Gateway

Responsible for:

- Centralized API entry point
- Request routing
- JWT validation
- Service discovery integration

### Service Registry

Eureka Server is used for service registration and discovery.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Cloud Gateway
- Netflix Eureka
- Spring Data JPA
- MySQL 8
- Docker
- Docker Compose
- Swagger/OpenAPI
- Postman

## Service Ports

| Service | Port |
|---|---:|
| API Gateway | 8080 |
| Auth Service | 8081 |
| Employee Service | 8082 |
| Service Registry | 8761 |
| MySQL | 3307 |

## Features

### Authentication & Security

- User login using JWT
- JWT-based authentication
- Refresh token support
- Logout and refresh-token invalidation
- Role-based authorization
- ADMIN and EMPLOYEE roles
- Protected employee APIs

### Employee Management

- Employee CRUD operations
- Request validation using Jakarta Bean Validation
- Field-level validation error responses
- Global exception handling
- Proper HTTP status codes
- Employee not-found handling

### Microservices

- API Gateway routing
- Eureka service discovery
- Independent microservices
- Separate MySQL databases
- Docker Compose deployment

### API Documentation

- Swagger/OpenAPI documentation
- Postman API collection

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

## Validation

Employee create and update requests are validated using Jakarta Bean Validation.

Examples:

- Employee code is required
- First name is required
- Last name is required
- Email format validation
- Phone number must contain 10 digits
- Joining date is required

Invalid requests return:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format"
  }
}

Swagger UI

Employee Service Swagger UI is available through the API Gateway:

http://localhost:8080/employee-service/swagger-ui/index.html

Postman Collection

The project includes a Postman collection for testing the APIs.

Location:

postman/HRMS-Microservices-API.postman_collection.json

Docker Setup
Prerequisites
Docker
Docker Compose
Start All Services

From the project root directory:
docker compose up --build

Stop All Services
docker compose down

Check Running Containers
docker compose ps

Running the Project
Start Docker Desktop.
Clone the repository.
Open the project root directory.
Start all services using Docker Compose.
Verify services in Eureka.
Use Swagger or Postman to test the APIs.
Testing

The following scenarios have been tested:

Successful user login
JWT authentication
Refresh token generation
Logout and refresh-token invalidation
ADMIN employee access
EMPLOYEE employee access
ADMIN-only employee create/update/delete
Unauthorized access returning 403 Forbidden
Invalid employee request returning 400 Bad Request
Valid employee creation returning 201 Created
Employee not-found handling
API Gateway routing
Project Structure
HRMS_PROJECT
│
├── auth-service
├── employee-service
├── gateway-service
├── service-registry
├── postman
│   └── HRMS-Microservices-API.postman_collection.json
│
├── docker-compose.yml
├── README.md
└── .gitignore
Future Enhancements
Centralized configuration using Spring Cloud Config
Distributed logging
Resilience and fault tolerance
Pagination and sorting for employee APIs
API versioning
CI/CD pipeline
Production-ready secret management
# Employee Lifecycle Management

A microservices-based system for managing the complete lifecycle of employees, including onboarding, HR management, promotions, and authentication. Built with Spring Boot, Spring Cloud Gateway, Kafka, and secured with JWT authentication.

---

## Architecture

```mermaid
graph TD
    subgraph Gateway
        APIGW[API Gateway]
    end
    subgraph Auth
        AUTH[Auth Service]
    end
    subgraph Employee
        EMP[Employee Service]
    end
    subgraph HCM
        HCM[HCM Service]
    end
    subgraph Promotion
        PROMO[Promotion Service]
    end
    USER((User))
    USER-->|HTTP/JWT|APIGW
    APIGW-->|/auth/*|AUTH
    APIGW-->|/employee-service/*|EMP
    APIGW-->|/hcm-service/*|HCM
    APIGW-->|/promotion-service/*|PROMO
    EMP-->|Kafka|HCM
    HCM-->|Kafka|PROMO
    PROMO-->|Kafka|EMP
```

---

## Services Overview

- **API Gateway**: Entry point for all clients. Handles routing, security, and header validation.
- **Auth Service**: Handles authentication and JWT token issuance.
- **Employee Service**: Manages employee records and lifecycle events.
- **HCM Service**: Handles HR-related events and records.
- **Promotion Service**: Evaluates and processes employee promotions.

---

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL (for Employee Service)
- Kafka (for event-driven communication)
- (Optional) Postman for API testing

---

## Setup & Run

### 1. Clone the Repository
```sh
git clone <repo-url>
cd employee-lifecycle-management
```

### 2. Start Dependencies
- **MySQL**: Ensure a database named `employeedb` exists and update credentials in `employee-service/src/main/resources/application.properties`.
- **Kafka**: Start Kafka and Zookeeper (see [Kafka Quickstart](https://kafka.apache.org/quickstart)).

### 3. Build All Services
```sh
mvn clean package
```

### 4. Run Each Service (in separate terminals)
```sh
# API Gateway
cd apigateway
./mvnw spring-boot:run

# Auth Service
cd ../auth-service
./mvnw spring-boot:run

# Employee Service
cd ../employee-service
./mvnw spring-boot:run

# HCM Service
cd ../hcm-service
./mvnw spring-boot:run

# Promotion Service
cd ../promotion-service
./mvnw spring-boot:run
```

---

## End-to-End Flow Testing

### 1. **Authenticate and Get JWT Token**
**Request:**
- **POST** `http://localhost:7071/auth/login`
- **Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- **Response:** `{ "token": "<JWT>" }`

### 2. **Create an Employee**
**Request:**
- **POST** `http://localhost:7070/employee-service/employees`
- **Headers:**
  - `Authorization: Bearer <JWT>`
  - `X-Correlation-ID: test-123`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "name": "John Doe",
  "department": "IT"
}
```

### 3. **Get All Employees**
**Request:**
- **GET** `http://localhost:7070/employee-service/employees`
- **Headers:**
  - `Authorization: Bearer <JWT>`
  - `X-Correlation-ID: test-123`

### 4. **Deactivate an Employee**
**Request:**
- **PATCH** `http://localhost:7070/employee-service/employees/{id}/deactivate`
- **Headers:**
  - `Authorization: Bearer <JWT>`
  - `X-Correlation-ID: test-123`

### 5. **Swagger/OpenAPI Docs**
- Each service exposes Swagger UI at `/swagger-ui.html` (e.g., `http://localhost:8081/swagger-ui.html` for Employee Service).

---

## Troubleshooting
- **JWT errors**: Ensure the secret in Auth Service and API Gateway match.
- **Kafka errors**: Ensure Kafka is running and topics are created.
- **MySQL errors**: Ensure the `id` column in `employee` table is `AUTO_INCREMENT`.
- **Swagger 500 errors**: Ensure Spring Boot and SpringDoc versions are compatible.
- **Port conflicts**: Make sure each service runs on its configured port.

---

## License

[Your License Here]

 

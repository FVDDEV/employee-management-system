# Employee Management System (EMS)

A production-grade **Employee Management System** built using **Spring Boot**, **Spring Security**, **RabbitMQ**, **MySQL**, and **Docker**.  
The system supports employee, department, and leave management with role-based access control and asynchronous notifications.

---

## Features Overview

### Core Functionalities
- Employee CRUD operations
- Department management
- Leave request & approval workflow
- Role-based authentication & authorization
- Email notification via RabbitMQ
- Dockerized deployment

### User Roles
| Role | Capabilities |
|----|----|
| **ADMIN** | Manage employees, departments, approve/reject leaves |
| **USER** | View own profile, apply leave, view own leave status |

---

## Architecture Overview

```
Client (Postman / Browser)
        |
        v
Spring Boot REST APIs
        |
        |-- MySQL (Persistence)
        |
        |-- RabbitMQ (Async Events)
                |
                v
        Notification Consumer
                |
                v
           Email (SMTP)
```

---

## Technology Stack
- Java 17
- Spring Boot 3.x
- Spring Security (Basic Auth)
- Spring Data JPA
- RabbitMQ
- MySQL 8
- Docker & Docker Compose
- JUnit 5 + Mockito
- JaCoCo

---

## Security Design

### Authentication
- Basic Authentication

### Authorization
- Role-based access (`ADMIN`, `USER`)

### Test Credentials

| Role | Username | Password |
|----|----|----|
| ADMIN | falguni.dattani.11@gmail.com | admin123 |
| USER | falgunidattani@yahoo.com | user123 |

---

## Setup Instructions

### Prerequisites
- Java 17
- Maven
- Docker & Docker Compose

### Run using Docker
```bash
docker-compose up --build
```

### Run Locally
```bash
mvn clean install
mvn spring-boot:run
```

---

## Testing

Run tests:
```bash
mvn clean test
```

Coverage report:
```
target/site/jacoco/index.html
```

---

## Notifications
- Employee Created
- Leave Applied
- Leave Approved / Rejected

Handled asynchronously using RabbitMQ.

---

## Postman Collection
Includes:
- All APIs
- Role-based tests
- Sample payloads

---

## Assumptions
- Basic Auth used (as per requirement)
- In-memory users
- Email simulated
- No UI layer

---

##  Final Submission
Provide:
1. https://github.com/FVDDEV/employee-management-system.git

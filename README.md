# Marketplace API (mAPI)

A high-performance, concurrency-safe e-commerce backend built with Java and Spring Boot. This service handles user 
authentication, product catalogs, and transactional order management, designed to survive heavy parallel checkout loads 
without inventory inconsistency.

## Tech Stack & Core Tools
* **Language:** Java 17
* **Framework:** Spring Boot 3 (Web, Data JPA)
* **Database:** PostgreSQL
* **Infrastructure:** Docker & Docker Compose
* **Testing:** JUnit 5, Mockito, Testcontainers

## Current Architecture Status (Stage 1)
This project is being built with a **test-driven mindset from day one**, focusing on deep architectural mastery, 
logging visibility, and robust error handling.

* **Domain Model:** Robust relational schema managing `User`, `Product`, `Order`, and `OrderItem` entities.
* **Logging Discipline:** Structured entry/exit and transactional logging tracing requests before and after database 
write operations.
* **Global Exception Handling:** Clean API response contracts handled gracefully via `@ControllerAdvice`.
* **Testing Strategy:** 
  * `Slice Testing (@WebMvcTest)` isolating controller web layer routing and validation mechanics.
  * `Integration Testing (@SpringBootTest + Testcontainers)` spawning real, ephemeral PostgreSQL instances in Docker 
containers to validate true database behavior.

## Project Setup & Execution

### Prerequisites
* Java 17
* Docker & Docker Compose
* Maven

### 1. Run the Application Localy (with PostgreSQL)
Bring up the managed infrastructure using Docker Compose:
```bash
docker-compose up -d
```
Run the Spring Boot application:
```bash
mvn spring-boot:run
```

### 2. Run the Test Suite
To verify endpoints and execute integration flows against a live Testcontainers instance:
```bash
mvn clean test
```

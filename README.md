# Marketplace API (mAPI)

A high-performance, concurrency-safe e-commerce backend built with Java and Spring Boot. This service handles user
management, product catalogs, and transactional order orchestration, designed to survive heavy parallel checkout loads
without inventory inconsistency.

## Tech Stack & Core Tools
* **Language:** Java 17+
* **Framework:** Spring Boot 3 (Web, Data JPA, Security)
* **Database:** PostgreSQL 16
* **Infrastructure:** Docker & Docker Compose
* **Testing:** JUnit 5, Mockito, Testcontainers

## Current Architecture Status (Stage 1 Complete)
This project is engineered with an **isolated testing mindset from day one**, focusing on deep framework intimacy,
transactional logging visibility, and robust error contracts.

* **Domain Model:** Decoupled relational engine managing `User`, `Product`, `Order`, and `OrderItem` entities.
* **Logging Discipline:** Structured tracing on boundaries and transactional boundaries capturing states before and after database writes.
* **Global Exception Handling:** Unified client contract envelope (`APIResponse<T>`) cleanly intercepted via `@ControllerAdvice`.
* **Security Layer:** Pluggable web security filter chains isolated out during heavy integration test lifecycles via targeted `TestSecurityConfig` architectures.
* **Testing Strategy:**
  * `Slice Testing (@WebMvcTest)` isolating controller web routing, validation constraints, and serialization handling without loading business containers.
  * `Integration Testing (@SpringBootTest + Testcontainers)` leveraging the Singleton Container Pattern to spawn ephemeral PostgreSQL Docker instances for lightweight, isolated end-to-end assertions.

## Project Setup & Execution

### Prerequisites
* Java 17 or higher
* Docker Desktop / Engine
* Maven

### 1. Run the Infrastructure Locally
Bring up the isolated database layer via Docker Compose (exposed on local port `6699` to safeguard system configuration conflicts):
```bash
docker-compose up -d
```
Run the Spring Boot application profile:
```bash
mvn spring-boot:run
```

### 2. Run the Test Suite
To verify controller slices and execute structural database integration queries against the Testcontainers engine:
```bash
mvn clean test
```
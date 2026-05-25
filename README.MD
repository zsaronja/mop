# Cart Service

Spring Boot mikroservis koji upravlja korisničkom košaricom (Cart Service) kao dio online maloprodajne platforme.

Servis koristi Redis kao primarni storage (cache-first pristup) te PostgreSQL kao fallback za trajnu pohranu podataka.

## Architecture Overview

- Microservice architecture (Spring Boot)
- REST API (synchronous communication)
- Redis-first data access pattern
- PostgreSQL persistence layer
- Containerized deployment (Docker)

## Prerequisites

- Java 17+
- Maven 3+
- Docker + Docker Compose

## Build

mvn clean package -DskipTests

## Running the Application

### Option 1 – Full Docker setup

    docker compose up --build

### Option 2 – Local development

    docker compose up -d
    mvn spring-boot:run

## Verify Services

    docker ps
    docker exec -it mop-cart-postgres psql -U user -d mop_cart_db

## Health check:

http://localhost:8080/actuator/health

## API Endpoints

GET /cart/{userId}
POST /cart/{userId}/items
DELETE /cart/{userId}/items/{productId}

## Example Request
POST http://localhost:8080/cart/1/items

## Tech Stack

- Java 17
- Spring Boot
- Redis
- PostgreSQL
- Docker

## Stop services

    docker compose down

## Summary

- Redis-first pristup za performanse
- PostgreSQL fallback za pouzdanost
- Docker-based deployment za jednostavno pokretanje

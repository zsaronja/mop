# Cart Service

    Spring Boot mikroservis koji upravlja korisničkom košaricom (Cart Service) kao dio online maloprodajne platforme.
    Servis koristi Redis kao primarni storage (cache-first pristup) te PostgreSQL kao fallback za trajnu pohranu podataka.


---

## Build Status

![Build](https://github.com/zsaronja/mop/actions/workflows/build.yml/badge.svg)

---

## Features

    - Keycloak authentication (JWT-based)
    - User identity extracted from JWT (preferred_username)
    - REST API za upravljanje košaricom
    - DTO-based arhitektura (bez izlaganja JPA entiteta)
    - Redis caching (DTO-based, bez JPA objekata u cache-u)
    - PostgreSQL kao primary data store
    - Kafka event publishing (ADD / REMOVE)
    - Circuit Breaker za Redis fallback
    - Global exception handling i validacija
    - Slojevita arhitektura (controller → service → repository)

---

## Architecture Overview

    - Microservice architecture (Spring Boot)
    - Stateless REST API
    - JWT authentication putem Keycloak-a
    - Redis se koristi kao read-through cache
    - PostgreSQL kao source of truth
    - Docker-based deployment
    - Kafka za asynchronous event-driven komunikaciju
    - Docker-based deployment
    - CI pipeline (GitHub Actions)

---

## Architecture Diagram

    Client → Controller → Service → Repository → PostgreSQL  
                            ↘  
                            Redis (cache)

---

## Tech Stack

    - Java 17
    - Spring Boot
    - Spring Security (OAuth2 Resource Server)
    - Keycloak
    - Redis
    - PostgreSQL
    - Apache Kafka
    - Docker
    - GitHub Actions (CI)

---

## Prerequisites

    - Docker + Docker Compose
    - Java 17 (optional za local run)
    - Maven 3+ (optional)

---

## Build

    ```bash
    mvn clean package -DskipTests

---

## Running the Application

### Option 1 – Full Docker setup

    docker compose up --build

### Option 2 – Local development

    docker compose up -d
    mvn spring-boot:run

---

## Verify Services

    docker ps

    ### PostgreSQL

    docker exec -it mop-cart-postgres psql -U user -d mop_cart_db

---

## Health check:

    http://localhost:8080/actuator/health

---

## Authentication

    Servis koristi JWT tokene izdane od Keycloak-a.

    - identitet korisnika dolazi iz JWT-a
    - preferred_username koristi se u response-u
    - stateless autentikacija (nema sessiona)

---

## API Endpoints

    Svi endpointi zahtijevaju JWT token.

### Get cart

    GET /cart

### Add item

    POST /cart/items

### Remove item

    DELETE /cart/items/{productId}?quantity=1

---

## Example Request

    ```http
    POST /cart/items
    Authorization: Bearer <JWT>

    {
    "productId": "eggs",
    "quantity": 2
    }

---

## Kafka Events

    Service emit events nakon uspješnog DB commit-a.

    Examples:
            
        {
        "userId": "test",
        "productId": "eggs",
        "quantity": 2,
        "action": "ADD"
        }

        {
        "userId": "test",
        "productId": "eggs",
        "quantity": 1,
        "action": "REMOVE"
        }


## Stop services

    docker compose down 

---

## Design Decisions

- DTO layer sprječava izlaganje JPA entiteta
- Redis koristi DTO, ne entity → nema lazy loading problema
- Database je single source of truth
- Cache se koristi samo za read operacije
- Domain logika je u Cart entity
- JPA relacije se održavaju pomoću lifecycle hookova (@PostLoad)
- Service layer ne sadrži business logiku
- Kafka event se šalje samo nakon uspješnog DB commit-a
- Fallback logika ne generira side-effecte

---

## Docker
    Servis se sastoji od:

    cart-service
    postgres
    redis
    kafka
    zookeeper

    Pokretanje:
        docker compose up --build

    Gašenje 
        docker compose down

## CI (Continuous Integration)
    Projekt koristi GitHub Actions za:

    build provjeru
    pokretanje testova
    Docker build

    Pokreće se automatski na svaki push i pull request.

## Summary

    - Redis caching za brze read operacije
    - PostgreSQL kao pouzdani storage
    - JWT autentikacija preko Keycloak-a
    - Event-driven arhitektura uz Kafka-u
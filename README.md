# Study Tracker API (Spring Boot + PostgreSQL)

REST API for the Study Tracker frontend. Provides CRUD endpoints for tasks and stores data in PostgreSQL.

## Live API
- Base URL (Render): https://study-tracker-backend-qlkv.onrender.com
- Tasks endpoint: https://study-tracker-backend-qlkv.onrender.com/api/tasks

## Endpoints
- `GET /api/tasks` — list tasks
- `POST /api/tasks` — create a task
- `PATCH /api/tasks/{id}` — toggle done
- `PUT /api/tasks/{id}` — update title
- `DELETE /api/tasks/{id}` — delete task

## Tech Stack
- Java + Spring Boot
- Spring Data JPA
- PostgreSQL (production)
- H2 (optional for local dev)

## Local Setup

### Requirements
- Java 17+
- Maven

### Run locally
```bash
./mvnw spring-boot:run
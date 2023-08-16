# Quoted Server

## Description

### Use Case

This is the server-side application for the [Quoted frontend Angular application](https://github.com/nicoluca/quoted-2-frontend).
It provides various REST endpoints for the frontend to consume, among which:
- User management
- Quote management
- Source management
- Export functionality

It also handles the database persistence of the application.

### Technologies used

- Java 20
- Spring Boot 3.1.1
  - Spring Security
  - Spring Data JPA
  - DevTools
  - Lombok
  - Actuator
  - etc.
- PostgreSQL
- [Okta](https://www.okta.com/) for user management
- Docker
- JUnit 5 (for testing)
- H2 (for testing)

## Setup

1. Set up PostgreSQL database and configure `application.properties` accordingly.
2. Run sql scripts in `sql` to create the database and sample tables.
3. Create an Okta account and configure `application.properties` accordingly.
2. Install via Maven: `mvn clean install`
2. Build Docker image: `docker build -t quoted-server .` (To build for amd64, add `--platform linux/amd64` to the command)
3. Run Docker image: `docker run -p 8080:8080 quoted-server` (or configure your port of choice)
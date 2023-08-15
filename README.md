# Quoted Server

## Description

This is the server-side application for the [Quoted frontend Angular application](https://github.com/nicoluca/quoted-2-frontend).
It provides various REST endpoints for the frontend to consume, among which:
- User management
- Quote management
- Source management
- Export functionality

## Setup requirements
- PostgreSQL database must be running and be configured in `application.properties` accordingly
- Docker must be installed and running (alternatively, run via Maven)

### Build and run locally

1. Install via Maven: `mvn clean install`
2. Build Docker image: `docker build -t quoted-server .`
3. Run Docker image: `docker run -p 8080:8080 quoted-server`
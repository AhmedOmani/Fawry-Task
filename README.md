# Travel Destination Planner

A full-stack travel destination planner built with Spring Boot and Angular. Admins can fetch destinations from REST Countries API and manage them in an internal database. Users can browse destinations and mark them as "Want to Visit".

## Tech Stack

- **Backend:** Java 17, Spring Boot 4, Spring Security (JWT), Spring Data JPA
- **Frontend:** Angular 19, TypeScript
- **Database:** H2 (file-based, persists across restarts) -> i choosed it to simplify the process of configuration and install postgres / mysql , the idea will still the same

## Project Structure

```
Fawry-Task/
  travel-managment/          # Spring Boot backend
  travel-managment-frontend/ # Angular frontend
```

## How to Run

### Prerequisites

- Java 17+
- Node.js 18+
- npm

### Option 1: Run Manually

**Backend:**

```bash
cd travel-managment
./mvnw spring-boot:run
```

The backend starts on http://localhost:8080

**Frontend:**

```bash
cd travel-managment-frontend
npm install
npx ng serve
```

The frontend starts on http://localhost:4200

### Option 2: Docker Compose

```bash
docker-compose up --build
```

This starts both services. Frontend on http://localhost:4200, backend on http://localhost:8080.

## Default Admin Account

On first run, the application seeds a default admin:

- **Email:** admin@fawry.com
- **Password:** admin123

## Documentation

For detailed API endpoints, thought process, and design decisions:
https://www.notion.so/Fawry-Task-336622ca530780e190d9f74b0ddb65e4

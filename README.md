# Salary Management System

Assessment project for an HR Manager who needs to manage salary information for 10,000 employees across multiple countries.

## Stack
- Java 21 + Spring Boot 3.4
- Angular 19
- SQLite
- JUnit 5 / Mockito
- Docker Compose

## Features
- Employee directory with pagination, name/ID/email search, country and department filters
- Employee detail API
- Update an employee's salary
- Salary summaries (count, average, minimum and maximum) grouped by currency
- Deterministic synthetic seed data for 10,000 employees
- Validation, not-found handling, unit/controller tests
- Docker setup for the API and Angular UI

> This is an assessment/demo app. It uses synthetic data and does not include production authentication, payroll payments, tax calculations, or compliance controls. Salary insights are grouped by currency so values in different currencies are not incorrectly combined.

## Run with Docker (recommended)
Prerequisite: Docker Desktop / Docker Engine with Compose.

From this folder:

```bash
docker compose up --build
```

Then open:
- UI: http://localhost:4200
- API health: http://localhost:8080/api/health
- API employee list: http://localhost:8080/api/employees

The first startup inserts 10,000 synthetic employees into SQLite. It may take a short moment. Data persists in the `salary-data` Docker volume. To reset the database and reseed:

```bash
docker compose down -v
docker compose up --build
```

Stop services with `Ctrl+C`, then `docker compose down`.

## Run locally without Docker
### Backend
Prerequisites: JDK 21 and Maven 3.9+.

```bash
cd backend
mvn spring-boot:run
```

API runs at `http://localhost:8080`. SQLite database defaults to `backend/salary.db`.

### Frontend
Prerequisites: Node.js 20.11+ and npm.

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200`. The Angular dev server proxies `/api` requests to `http://localhost:8080`.

## Run tests
```bash
cd backend
mvn test
```

Frontend build check:
```bash
cd frontend
npm install
npm run build
```

## API overview
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/employees?page=0&size=20&search=&country=&department=` | Search/filter/paginate employees |
| GET | `/api/employees/{id}` | Employee details |
| PUT | `/api/employees/{id}/salary` | Update salary, body `{ "salary": 75000.00 }` |
| GET | `/api/salary/summary?country=&department=` | Summary by currency |
| GET | `/api/meta/filters` | Distinct country and department options |

Page numbering starts at 0. Maximum page size is 100. Salary summary is grouped by currency; when country is selected, the result is scoped to that country. Amounts are not added across currencies.

## Project structure
```text
backend/   Spring Boot API, SQLite access, seed generator, tests
frontend/  Angular UI
 docs/     requirements, architecture, trade-offs, AI usage, demo script
```

## Suggested Git workflow
Make small, meaningful commits as you work rather than one commit at the end. Example sequence:
1. `docs: add requirements and initial design`
2. `feat: add employee API and SQLite schema`
3. `feat: seed 10000 synthetic employees`
4. `test: cover employee and salary service behavior`
5. `feat: add Angular salary dashboard`
6. `build: add Docker Compose deployment`
7. `docs: add setup and demo instructions`

If this folder is not already a Git repository, run `git init`, then add and commit the files in stages.

## Before submission
- Run backend tests and frontend build locally.
- Start the full app and verify search, filters, pagination, salary summary and salary update.
- Record your own screen demo using `docs/DEMO_SCRIPT.md` (a real recording is not included in this source archive).
- Review all code and be ready to explain the decisions in `docs/TRADE_OFFS.md`.

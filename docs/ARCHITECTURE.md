# Architecture and data model

## Components

```text
Angular SPA (port 4200)
       | HTTP /api
       v
Spring Boot REST API (port 8080)
       | JDBC / parameterized SQL
       v
SQLite database file (salary.db)
```

The Angular development server proxies `/api` to Spring Boot. In Docker, Nginx serves the built Angular app and proxies `/api` to the backend container.

## Backend layers
- `controller`: HTTP routes and request/response boundaries.
- `service`: business validation and salary update rules.
- `repository`: parameterized SQL through Spring `JdbcTemplate`.
- `config`: schema initialization and deterministic seed data.
- `dto`: API response/request records.

## Employee table
| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key |
| employee_code | TEXT | Unique, indexed business identifier |
| first_name / last_name | TEXT | Synthetic name |
| email | TEXT | Unique |
| department | TEXT | Department filter |
| country | TEXT | Country filter |
| job_title | TEXT | Role/title |
| currency | TEXT | Salary currency code |
| salary | NUMERIC | Non-negative amount |
| updated_at | TEXT | ISO timestamp |

Indexes are created for employee code, country + department and name/email search fields. Page size is capped to avoid returning the entire directory in one response.

## Key endpoints
- `GET /api/employees`: query parameters `page`, `size`, `search`, `country`, `department`.
- `GET /api/employees/{id}`: employee details.
- `PUT /api/employees/{id}/salary`: update a salary.
- `GET /api/salary/summary`: aggregates grouped by currency.
- `GET /api/meta/filters`: filter option lists.

## Main decisions
- SQLite keeps the assessment easy to run with no separate database server.
- JDBC is used instead of ORM-specific SQLite dialect setup, keeping SQL and indexes explicit.
- Salary summaries are grouped by currency to avoid misleading cross-currency arithmetic.
- Synthetic seed generation uses a fixed random seed so local results are reproducible.

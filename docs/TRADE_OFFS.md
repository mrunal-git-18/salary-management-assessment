# Design trade-offs and performance notes

1. **SQLite vs PostgreSQL:** SQLite reduces setup friction and is suitable for this single-user assessment. It is not the recommended choice for a highly concurrent, multi-instance production HR platform. A production deployment should move to PostgreSQL and add migrations.
2. **JDBC vs JPA:** `JdbcTemplate` makes SQLite SQL, indexes, filtering and aggregate queries explicit. It trades ORM conveniences for predictability and a small dependency surface.
3. **Pagination:** The API returns a bounded page (maximum 100 records) instead of loading all 10,000 records into the browser. Search/filter queries are parameterized and relevant columns are indexed.
4. **Salary currency:** Salaries are grouped by currency. Averaging or summing INR, USD, GBP, etc. without exchange-rate conversion would be misleading. Exchange-rate conversion is out of scope.
5. **Synthetic data:** Seed data is deterministic and fictional. This supports reproducible demos and avoids exposing real employee data.
6. **Security:** Authentication, authorization, encryption policy, audit trail, secrets management and privacy review are omitted for the assessment, not because they are optional in production. Do not use this demo with real compensation data.
7. **Deployment:** Docker Compose makes the app reproducible on a laptop. It is a local/demo deployment, not a production high-availability setup.

## Potential next steps
- PostgreSQL and schema migrations (Flyway/Liquibase).
- Authentication, HR roles and audit logging for salary changes.
- Optimistic locking / change history for concurrent salary updates.
- API integration tests against a temporary SQLite database.
- CI workflow for test and build verification.

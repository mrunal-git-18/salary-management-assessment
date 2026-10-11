# Salary Management System — Requirements

## 1. Goal
Build a web-based salary management application for an HR Manager supporting an organization of approximately 10,000 employees across multiple countries. Replace the current spreadsheet-driven workflow with searchable employee records and understandable compensation insights.

## 2. User and problem
**Primary persona:** HR Manager.

The HR team maintains salary information in Excel. Finding a particular employee and answering organization-level compensation questions is tedious. The application centralizes the records and provides efficient search and salary summaries.

## 3. In scope
- List employee records with pagination.
- Search by employee code, name or email.
- Filter by country and department.
- View an employee's role, department, country, currency and salary.
- Update an employee's salary.
- Show employee count, average, minimum and maximum salary, grouped by currency.
- Seed 10,000 deterministic synthetic employees.
- Validate input and return clear HTTP errors.
- Provide automated tests, setup documentation and a demo walkthrough.

## 4. Out of scope
- Payroll execution, bank transfers and payslip generation.
- Tax, benefits and statutory deductions.
- Attendance, leave and performance management.
- External HRIS integration.
- Production identity management / role-based access control.

These are excluded to keep the assessment focused on salary-data retrieval and basic maintenance. Authentication and audit logging would be required before real salary data could be used in production.

## 5. Quality attributes
- Maintainable layered code and meaningful names.
- Deterministic seed data and tests.
- Safe parameterized database queries.
- Pagination and indexes for a 10,000-record directory.
- Salary aggregates never mix different currencies.
- Reproducible local setup and containerized run option.

## 6. Acceptance criteria
1. Application runs end-to-end with an Angular UI and Spring Boot API.
2. SQLite contains 10,000 synthetic employees on first startup.
3. HR can search, filter, paginate and open employee records.
4. Salary update rejects negative or invalid values.
5. Salary summaries show count, average, minimum and maximum grouped by currency.
6. Core backend behavior is covered by automated tests.
7. Repository includes requirements, architecture, trade-offs, AI-use notes and demo instructions.
8. A human-recorded demo shows the running application.

## 7. Assumptions
- All seed data is fictional.
- Salary is represented as a decimal amount with an ISO-like currency code.
- When multiple currencies are present, aggregates are grouped by currency instead of summing incomparable amounts.
- The initial assessment build has no authentication and must not be used with real employee data.

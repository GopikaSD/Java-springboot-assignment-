# REST API Design - Day 3

## HTTP Methods and Status Codes

| Operation | HTTP Method | Endpoint | Success Code | Failure Code |
|---|---|---|---|---|
| Get all employees | GET | /api/v1/employees | 200 | 500 |
| Get employee by ID | GET | /api/v1/employees/{id} | 200 | 404 |
| Filter by department | GET | /api/v1/employees?department=IT | 200 | 400 |
| Create employee | POST | /api/v1/employees | 201 | 400 |
| Update employee | PUT | /api/v1/employees/{id} | 200 | 404 |
| Delete employee | DELETE | /api/v1/employees/{id} | 204 | 404 |

## REST and HTTP Concepts

- GET is used to read employee data.
- POST is used to create a new employee.
- PUT is used to update employee data.
- DELETE is used to delete an employee.
- Safe methods do not modify resource state.
- Idempotent methods produce the same final state when repeated.
- HTTP 200 means success.
- HTTP 201 means resource created.
- HTTP 400 means bad request.
- HTTP 404 means resource not found.
- HTTP 409 means conflict.
- HTTP 500 means internal server error.
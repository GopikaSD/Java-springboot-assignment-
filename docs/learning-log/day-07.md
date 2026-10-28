
Day 7 - Error Handling

Completed Tasks
- Created custom exceptions: EmployeeNotFoundException, DuplicateEmailException, and InvalidOperationException.
- Created ErrorResponse record for a consistent error response format.
- Used GlobalExceptionHandler with @RestControllerAdvice.
- Handled employee not found errors with HTTP 404.
- Handled duplicate email errors with HTTP 409.
- Handled validation errors with HTTP 400 and fieldErrors.
- Avoided exposing internal exception details to API clients.

Postman Testing
- GET non-existent employee ID: 404 Not Found
- POST duplicate email: 409 Conflict
- POST invalid employee fields: 400 Bad Request

Self-Review
I learned how custom exceptions and global exception handling work in Spring Boot. I tested different error cases using Postman and verified the common ErrorResponse JSON structure.

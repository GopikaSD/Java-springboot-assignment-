Day 10 - Spring Data JPA Repository

Completed Tasks

1. Implemented Spring Data JPA derived query methods.
2. Added department search using findByDepartmentIgnoreCase.
3. Added last name search using findByLastNameContainingIgnoreCase.
4. Added duplicate email validation.
5. Added duplicate employee code validation.
6. Implemented pagination using Pageable and Page.
7. Implemented sorting for employee records.
8. Seeded 25 sample employees using CommandLineRunner.
9. Configured DataSeeder to run only in dev profile.
10. Prevented duplicate data seeding on application restart.
11. Enabled Hibernate SQL logging.
12. Added global exception handling for duplicate records.

Postman Testing

- Get All Employees - Passed
- Search Employee by Name - Passed
- Search Employees by Department - Passed
- Department Case Insensitive Search - Passed
- Create Employee - 201 Passed
- Create Employee 2 - 201 Passed
- Update Employee - 200 Passed
- Delete Employee - 204 Passed
- Get Employee by ID - 200 Passed
- Duplicate Email - 409 Passed
- Duplicate Employee Code - 409 Passed
- Pagination and sorting - Verified

Sample Data

Successfully seeded 25 sample employees.

SQL Logging

Hibernate SQL logging enabled and verified.

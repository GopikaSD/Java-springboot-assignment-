Day 5 – Layered Architecture, DTOs and MapStruct

Completed Tasks

- Created the EmployeeRepository interface and InMemoryEmployeeRepository implementation.
- Moved employee business logic into EmployeeService.
- Used constructor injection for dependencies.
- Created EmployeeRequest, EmployeeResponse and UpdateEmployeeRequest records.
- Used MapStruct to map Employee entities to DTOs and DTOs to entities.
- Added employee create, update and delete API operations.

API Testing

- GET all employees – Successful
- GET employee by ID – Successful
- Filter employees by department – Successful
- Search employees by name – Successful
- POST create employee – 201 Created
- PUT update employee – 200 OK
- DELETE employee – 204 No Content

Learning
Learned how to separate controller, service and repository layers, use DTOs to transfer data, and map objects with MapStruct.

Notes
Tested the API endpoints using Postman.


Day 11 - PostgreSQL and Entity Relationships

Completed Tasks

1. PostgreSQL Configuration
- Configured PostgreSQL as the local database.
- Used Spring profiles to separate H2 development and PostgreSQL local configurations.
- Verified application startup with PostgreSQL.

2. Employee and Address Relationship
- Implemented a One-to-One relationship between Employee and Address.
- Used `@OneToOne` and `@JoinColumn`.
- Verified that employee address details are saved and retrieved successfully.

3. Department and Employee Relationship
- Created the Department entity.
- Implemented a One-to-Many relationship between Department and Employee.
- Used `@OneToMany` in Department.
- Used `@ManyToOne` in Employee.
- Created DepartmentRepository and DepartmentController.


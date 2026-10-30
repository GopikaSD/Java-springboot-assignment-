
package com.example.employeemanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employees by department
    List<Employee> findByDepartmentIgnoreCase(String department);

    // Find employees by department with pagination
    Page<Employee> findByDepartmentIgnoreCase(
            String department,
            Pageable pageable
    );

    // Find employees by last name
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    // Find employees by last name with pagination
    Page<Employee> findByLastNameContainingIgnoreCase(
            String lastName,
            Pageable pageable
    );

    // Check duplicate email
    boolean existsByEmail(String email);

    // Check duplicate employee code
    boolean existsByEmployeeCode(String employeeCode);

    // Existing case-insensitive duplicate checks
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

    // Search first name or last name
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    // Search first name or last name with pagination
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    // Search by department and first name or department and last name
    Page<Employee> findByDepartmentIgnoreCaseAndFirstNameContainingIgnoreCaseOrDepartmentIgnoreCaseAndLastNameContainingIgnoreCase(
            String department1,
            String firstName,
            String department2,
            String lastName,
            Pageable pageable
    );
}

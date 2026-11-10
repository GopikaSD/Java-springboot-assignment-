
package com.example.employeemanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employees by department name
    List<Employee> findByDepartment_NameIgnoreCase(String department);

    // Find employees by department name with pagination
    Page<Employee> findByDepartment_NameIgnoreCase(
            String department,
            Pageable pageable
    );

    // Search employees by department and first name or last name
    Page<Employee>
    findByDepartment_NameIgnoreCaseAndFirstNameContainingIgnoreCaseOrDepartment_NameIgnoreCaseAndLastNameContainingIgnoreCase(
            String department1,
            String firstName,
            String department2,
            String lastName,
            Pageable pageable
    );

    // Find employees by department ID
    List<Employee> findByDepartment_Id(Long departmentId);

    // Find employees by last name
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    Page<Employee> findByLastNameContainingIgnoreCase(
            String lastName,
            Pageable pageable
    );

    // Check duplicate email
    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    // Check duplicate employee code
    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

    // Search first name or last name
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    // Find employees by project name using JPQL
    @Query("""
           SELECT DISTINCT e
           FROM Employee e
           JOIN e.projects p
           WHERE LOWER(p.name) = LOWER(:projectName)
           """)
    List<Employee> findEmployeesByProjectName(
            @Param("projectName") String projectName
    );
}

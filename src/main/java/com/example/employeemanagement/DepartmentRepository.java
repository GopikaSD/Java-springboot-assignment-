
package com.example.employeemanagement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    // Find department by name (case-insensitive)
    Optional<Department> findByNameIgnoreCase(String name);

    // Fetch departments with employees using EntityGraph
    @EntityGraph(attributePaths = {"employees"})
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithEmployees();

    // Fetch departments with employees using JOIN FETCH
    @Query("""
           SELECT DISTINCT d
           FROM Department d
           LEFT JOIN FETCH d.employees
           """)
    List<Department> findAllWithEmployeesUsingJoinFetch();

    // Top 5 departments by employee count using Native SQL
    @Query(value = """
            SELECT
                d.name AS departmentName,
                COUNT(e.id) AS employeeCount
            FROM departments d
            LEFT JOIN employees e
                ON e.department_id = d.id
            GROUP BY d.id, d.name
            ORDER BY COUNT(e.id) DESC
            LIMIT 5
            """, nativeQuery = true)
    List<DepartmentEmployeeCountProjection> findTop5DepartmentsByEmployeeCount();
}

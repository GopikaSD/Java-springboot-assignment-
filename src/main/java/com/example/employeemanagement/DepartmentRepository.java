
package com.example.employeemanagement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    // Find department by name (case-insensitive)
    Optional<Department> findByNameIgnoreCase(String name);
}

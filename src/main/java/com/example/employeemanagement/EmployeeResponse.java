
package com.example.employeemanagement;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        String fullName
) {
}


package com.example.employeemanagement;

public record EmployeeRequest(
        String firstName,
        String lastName,
        String email,
        String department
) {
}

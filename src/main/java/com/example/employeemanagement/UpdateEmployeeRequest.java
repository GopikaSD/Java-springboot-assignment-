
package com.example.employeemanagement;

public record UpdateEmployeeRequest(
        String firstName,
        String lastName,
        String email,
        String department
) {
}

package com.example.employeemanagement;

public record Employee(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
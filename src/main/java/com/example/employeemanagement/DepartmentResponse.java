package com.example.employeemanagement;

import java.util.List;

public record DepartmentResponse(
        Long id,
        String name,
        String location,
        List<EmployeeResponse> employees
) {
}
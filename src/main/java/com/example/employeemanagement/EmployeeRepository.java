package com.example.employeemanagement;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    public String getRepositoryName() {
        return "EmployeeRepository";
    }
}
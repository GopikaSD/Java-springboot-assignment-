package com.example.employeemanagement;

public class DuplicateEmployeeCodeException extends RuntimeException {

    public DuplicateEmployeeCodeException(String message) {
        super(message);
    }
}
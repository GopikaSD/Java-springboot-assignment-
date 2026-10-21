package com.example.employeemanagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Value("${app.message}")
    private String message;

    @Value("${app.company}")
    private String company;

    public String getMessage() {
        return message;
    }

    public String getCompany() {
        return company;
    }

    public String greet(String name) {
        return "Hello " + name + "! Welcome to " + company;
    }
}

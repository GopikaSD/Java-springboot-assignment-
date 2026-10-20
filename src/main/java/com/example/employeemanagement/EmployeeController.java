package com.example.employeemanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @GetMapping("/sample")
    public Employee getSampleEmployee() {
        return new Employee(
                1L,
                "Gopika",
                "S",
                "gopika@example.com"
        );
    }
}
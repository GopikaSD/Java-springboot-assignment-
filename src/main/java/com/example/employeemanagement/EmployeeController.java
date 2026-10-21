package com.example.employeemanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/sample")
    public Employee getSampleEmployee() {
        return new Employee(
                1L,
                "Gopika",
                "S",
                "gopika@example.com"
        );
    }

    @GetMapping("/message")
    public String getMessage() {
        return employeeService.getMessage();
    }

    @GetMapping("/company")
    public String getCompany() {
        return employeeService.getCompany();
    }

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return employeeService.greet(name);
    }
}

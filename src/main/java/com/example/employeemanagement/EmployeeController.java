package com.example.employeemanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
                "gopika@example.com",
                "IT"
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

    @GetMapping
    public List<Employee> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        if (page < 0 || size <= 0) {
            return List.of();
        }

        List<Employee> employees =
                new ArrayList<>(employeeService.getAllEmployees());

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        boolean descending = sortParts.length > 1
                && sortParts[1].equalsIgnoreCase("desc");

        if (sortField.equalsIgnoreCase("lastName")) {
            Comparator<Employee> comparator =
                    Comparator.comparing(Employee::lastName);

            if (descending) {
                comparator = comparator.reversed();
            }

            employees.sort(comparator);
        }

        long startLong = (long) page * size;

        if (startLong >= employees.size()) {
            return List.of();
        }

        int start = (int) startLong;
        int end = (int) Math.min(startLong + size, employees.size());

        return employees.subList(start, end);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/department")
    public List<Employee> getEmployeesByDepartment(
            @RequestParam String department) {

        return employeeService.getEmployeesByDepartment(department);
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam String name) {

        return employeeService.searchEmployeesByName(name);
    }
}
package com.example.employeemanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/sample")
    public Employee getSampleEmployee() {
        return new Employee(1L, "Gopika", "S",
                "gopika@example.com", "IT");
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
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/department")
    public List<Employee> getEmployeesByDepartment(
            @RequestParam String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return employeeService.searchEmployeesByName(name);
    }

    // Day 4: Create employee
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {

        if (employeeService.existsByEmail(employee.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        Employee created = employeeService.createEmployee(employee);

        return ResponseEntity
                .created(java.net.URI.create(
                        "/api/v1/employees/" + created.id()))
                .body(created);
    }

    // Day 4: Replace employee details
    @PutMapping("/{id}")
    public ResponseEntity<?> replaceEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        Employee existing = employeeService.getEmployeeById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existing.email().equalsIgnoreCase(employee.email())
                && employeeService.existsByEmail(employee.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        Employee updated = new Employee(
                id,
                employee.firstName(),
                employee.lastName(),
                employee.email(),
                employee.department()
        );

        employeeService.updateEmployee(updated);

        return ResponseEntity.ok(updated);
    }

    // Day 4: Update selected fields
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchEmployee(
            @PathVariable Long id,
            @RequestBody Map<String, Object> changes) {

        Employee existing = employeeService.getEmployeeById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String firstName = existing.firstName();
        String lastName = existing.lastName();
        String email = existing.email();
        String department = existing.department();

        if (changes.containsKey("firstName")) {
            firstName = String.valueOf(changes.get("firstName"));
        }

        if (changes.containsKey("lastName")) {
            lastName = String.valueOf(changes.get("lastName"));
        }

        if (changes.containsKey("email")) {
            email = String.valueOf(changes.get("email"));

            if (!existing.email().equalsIgnoreCase(email)
                    && employeeService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already exists");
            }
        }

        if (changes.containsKey("department")) {
            department = String.valueOf(changes.get("department"));
        }

        Employee updated = new Employee(
                id, firstName, lastName, email, department);

        employeeService.updateEmployee(updated);

        return ResponseEntity.ok(updated);
    }

    // Day 4: Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        boolean deleted = employeeService.deleteEmployee(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
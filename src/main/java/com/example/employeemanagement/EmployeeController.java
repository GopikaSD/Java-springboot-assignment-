
package com.example.employeemanagement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/sample")
    public EmployeeResponse getSampleEmployee() {
        return employeeService.getEmployeeById(1L);
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
    public List<EmployeeResponse> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        if (page < 0 || size <= 0) {
            throw new InvalidOperationException(
                    "Page must be zero or greater and size must be positive");
        }

        List<EmployeeResponse> employees =
                employeeService.getAllEmployees();

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        boolean descending = sortParts.length > 1
                && sortParts[1].equalsIgnoreCase("desc");

        if (sortField.equalsIgnoreCase("lastName")) {
            Comparator<EmployeeResponse> comparator =
                    Comparator.comparing(
                            EmployeeResponse::lastName,
                            String.CASE_INSENSITIVE_ORDER);

            if (descending) {
                comparator = comparator.reversed();
            }

            employees = employees.stream()
                    .sorted(comparator)
                    .toList();
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
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long id) {

        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/department")
    public List<EmployeeResponse> getEmployeesByDepartment(
            @RequestParam String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    @GetMapping("/search")
    public List<EmployeeResponse> searchEmployees(
            @RequestParam String name) {
        return employeeService.searchEmployeesByName(name);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse created =
                employeeService.createEmployee(request);

        return ResponseEntity
                .created(URI.create("/api/v1/employees/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> replaceEmployee(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        EmployeeResponse updated =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> patchEmployee(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        return replaceEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

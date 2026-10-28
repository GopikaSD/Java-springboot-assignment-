
package com.example.employeemanagement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

    // Task 8.1: Pagination and sorting
    @GetMapping
    public PageResponse<EmployeeResponse> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        validatePageAndSize(page, size);

        List<EmployeeResponse> employees =
                employeeService.getAllEmployees();

        return createPage(employees, page, size, sort);
    }

    // Task 8.2: Search by name and optional department
    @GetMapping("/search")
    public PageResponse<EmployeeResponse> searchEmployees(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        validatePageAndSize(page, size);

        String searchName = name.trim().toLowerCase(Locale.ROOT);

        String searchDepartment = department == null
                ? null
                : department.trim().toLowerCase(Locale.ROOT);

        List<EmployeeResponse> filtered =
                employeeService.getAllEmployees()
                        .stream()
                        .filter(employee ->
                                searchName.isEmpty()
                                || (employee.firstName() != null
                                && employee.firstName()
                                        .toLowerCase(Locale.ROOT)
                                        .contains(searchName))
                                || (employee.lastName() != null
                                && employee.lastName()
                                        .toLowerCase(Locale.ROOT)
                                        .contains(searchName)))
                        .filter(employee ->
                                searchDepartment == null
                                || searchDepartment.isEmpty()
                                || (employee.department() != null
                                && employee.department()
                                        .toLowerCase(Locale.ROOT)
                                        .equals(searchDepartment)))
                        .toList();

        return createPage(filtered, page, size, sort);
    }

    private void validatePageAndSize(int page, int size) {
        if (page < 0) {
            throw new InvalidOperationException(
                    "Page must be zero or greater");
        }

        if (size < 1 || size > 100) {
            throw new InvalidOperationException(
                    "Size must be between 1 and 100");
        }
    }

    private PageResponse<EmployeeResponse> createPage(
            List<EmployeeResponse> employees,
            int page,
            int size,
            String sort) {

        String[] sortParts = sort.split(",");

        String sortField = sortParts[0].trim();

        if (sortParts.length > 2
                || !sortField.equalsIgnoreCase("lastName")) {
            throw new InvalidOperationException(
                    "Supported sort field: lastName");
        }

        String direction = sortParts.length == 2
                ? sortParts[1].trim()
                : "asc";

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new InvalidOperationException(
                    "Sort direction must be asc or desc");
        }

        Comparator<EmployeeResponse> comparator =
                Comparator.comparing(
                        EmployeeResponse::lastName,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER));

        if (direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        List<EmployeeResponse> sorted = employees.stream()
                .sorted(comparator)
                .toList();

        long totalElements = sorted.size();

        int totalPages = totalElements == 0
                ? 0
                : (int) Math.ceil((double) totalElements / size);

        long startLong = (long) page * size;

        List<EmployeeResponse> content;

        if (startLong >= totalElements) {
            content = List.of();
        } else {
            int start = (int) startLong;
            int end = (int) Math.min(startLong + size, totalElements);
            content = sorted.subList(start, end);
        }

        return new PageResponse<>(
                content,
                page,
                size,
                totalElements,
                totalPages);
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

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse created =
                employeeService.createEmployee(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/v1/employees/" + created.id()))
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

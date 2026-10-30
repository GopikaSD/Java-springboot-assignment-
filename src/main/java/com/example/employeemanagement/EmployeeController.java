
package com.example.employeemanagement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(
            EmployeeService employeeService,
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
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

    // Get all employees using database pagination
    @GetMapping
    public PageResponse<EmployeeResponse> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        return employeeService.getAllEmployees(page, size, sort);
    }

    // Search employees using database pagination
    @GetMapping("/search")
    public PageResponse<EmployeeResponse> searchEmployees(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        if (page < 0) {
            throw new InvalidOperationException(
                    "Page must be zero or greater");
        }

        if (size < 1 || size > 100) {
            throw new InvalidOperationException(
                    "Size must be between 1 and 100");
        }

        String[] sortParts = sort.split(",");

        if (sortParts.length != 2
                || !sortParts[0].trim().equalsIgnoreCase("lastName")) {
            throw new InvalidOperationException(
                    "Sort must be lastName,asc or lastName,desc");
        }

        Sort.Direction direction;

        try {
            direction = Sort.Direction.fromString(
                    sortParts[1].trim());
        } catch (IllegalArgumentException exception) {
            throw new InvalidOperationException(
                    "Sort direction must be asc or desc");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, "lastName")
        );

        String searchName = name == null ? "" : name.trim();
        String selectedDepartment =
                department == null ? "" : department.trim();

        Page<Employee> employeePage;

        if (!searchName.isEmpty() && !selectedDepartment.isEmpty()) {
            // Search by name first, then filter by department in database
            employeePage = employeeRepository
                    .findByDepartmentIgnoreCaseAndFirstNameContainingIgnoreCaseOrDepartmentIgnoreCaseAndLastNameContainingIgnoreCase(
                            selectedDepartment,
                            searchName,
                            selectedDepartment,
                            searchName,
                            pageable
                    );
        } else if (!selectedDepartment.isEmpty()) {
            employeePage = employeeRepository.findByDepartmentIgnoreCase(
                    selectedDepartment,
                    pageable
            );
        } else {
            employeePage = employeeService.searchEmployeesByName(
                    searchName,
                    pageable
            ).content().isEmpty()
                    ? Page.empty(pageable)
                    : null;

            // Fetch the page directly so total counts remain accurate,
            // including when a requested page has no matching content.
            if (searchName.isEmpty()) {
                employeePage = employeeRepository.findAll(pageable);
            } else {
                employeePage =
                        employeeRepository
                                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                        searchName,
                                        searchName,
                                        pageable
                                );
            }
        }

        List<EmployeeResponse> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
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

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request));
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

package com.example.employeemanagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Create Department
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @RequestBody DepartmentRequest request) {

        DepartmentResponse response =
                departmentService.createDepartment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get All Departments
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {

        return ResponseEntity.ok(
                departmentService.getAllDepartments()
        );
    }

    // Get Department by ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @PathVariable Long id) {

        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Employees by Department
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(
            @PathVariable Long id) {

        return departmentService.getEmployeesByDepartment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Top 5 Departments by Employee Count
    @GetMapping("/top-5-by-employee-count")
    public ResponseEntity<List<DepartmentEmployeeCountProjection>>
            getTop5DepartmentsByEmployeeCount() {

        return ResponseEntity.ok(
                departmentService.getTop5DepartmentsByEmployeeCount()
        );
    }
}
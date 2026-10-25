
package com.example.employeemanagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Value("${app.message}")
    private String message;

    @Value("${app.company}")
    private String company;

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public String getMessage() {
        return message;
    }

    public String getCompany() {
        return company;
    }

    public String greet(String name) {
        return "Hello " + name + "! Welcome to " + company;
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id);
        return employee == null ? null : employeeMapper.toResponse(employee);
    }

    public List<EmployeeResponse> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public List<EmployeeResponse> searchEmployeesByName(String name) {
        return employeeRepository.searchByName(name).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        long nextId = employeeRepository.findAll().stream()
                .mapToLong(Employee::id)
                .max()
                .orElse(0L) + 1;

        Employee employee = employeeMapper.toEntity(request, nextId);
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toResponse(saved);
    }

    public EmployeeResponse updateEmployee(
            Long id, UpdateEmployeeRequest request) {

        Employee existing = employeeRepository.findById(id);

        if (existing == null) {
            return null;
        }

        if (!existing.email().equalsIgnoreCase(request.email())
                && existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Employee updated = new Employee(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department()
        );

        employeeRepository.update(updated);
        return employeeMapper.toResponse(updated);
    }

    public boolean deleteEmployee(Long id) {
        return employeeRepository.deleteById(id);
    }
}

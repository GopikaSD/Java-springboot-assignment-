
package com.example.employeemanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeService.class);

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

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id);
        }

        return employeeMapper.toResponse(employee);
    }

    public List<EmployeeResponse> getEmployeesByDepartment(
            String department) {
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
            throw new DuplicateEmailException("Email already exists");
        }

        long nextId = employeeRepository.findAll().stream()
                .mapToLong(Employee::id)
                .max()
                .orElse(0L) + 1;

        Employee employee = employeeMapper.toEntity(request, nextId);
        Employee saved = employeeRepository.save(employee);

        logger.info(
                "Employee created successfully with ID: {}",
                saved.id());

        return employeeMapper.toResponse(saved);
    }

    public EmployeeResponse updateEmployee(
            Long id, UpdateEmployeeRequest request) {

        Employee existing = employeeRepository.findById(id);

        if (existing == null) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id);
        }

        if (!existing.email().equalsIgnoreCase(request.email())
                && existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email already exists");
        }

        Employee updated = new Employee(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department(),
                request.phone(),
                request.joiningDate(),
                request.salary(),
                request.employeeCode()
        );

        employeeRepository.update(updated);

        logger.info(
                "Employee updated successfully with ID: {}",
                id);

        return employeeMapper.toResponse(updated);
    }

    public boolean deleteEmployee(Long id) {
        boolean deleted = employeeRepository.deleteById(id);

        if (!deleted) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id);
        }

        logger.info(
                "Employee deleted successfully with ID: {}",
                id);

        return true;
    }
}

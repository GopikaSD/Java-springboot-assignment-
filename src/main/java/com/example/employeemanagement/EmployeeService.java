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

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.searchByName(name);
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.update(employee);
    }

    public boolean deleteEmployee(Long id) {
        return employeeRepository.deleteById(id);
    }
}
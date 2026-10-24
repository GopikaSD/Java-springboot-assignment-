package com.example.employeemanagement;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.ArrayList;

@Repository
public class EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        employees.add(new Employee(1L, "Gopika", "S", "gopika@example.com", "IT"));
        employees.add(new Employee(2L, "Arun", "K", "arun@example.com", "HR"));
        employees.add(new Employee(3L, "Priya", "R", "priya@example.com", "IT"));
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee findById(Long id) {
        return employees.stream()
                .filter(employee -> employee.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Employee> findByDepartment(String department) {
        return employees.stream()
                .filter(employee -> employee.department()
                        .equalsIgnoreCase(department))
                .toList();
    }

    public List<Employee> searchByName(String name) {
        return employees.stream()
                .filter(employee ->
                        employee.firstName().toLowerCase()
                                .contains(name.toLowerCase())
                        || employee.lastName().toLowerCase()
                                .contains(name.toLowerCase()))
                .toList();
    }

    public boolean existsByEmail(String email) {
        return employees.stream()
                .anyMatch(employee ->
                        employee.email().equalsIgnoreCase(email));
    }

    public Employee save(Employee employee) {
        employees.add(employee);
        return employee;
    }

    public void update(Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id().equals(updatedEmployee.id())) {
                employees.set(i, updatedEmployee);
                return;
            }
        }
    }

    public boolean deleteById(Long id) {
        return employees.removeIf(employee ->
                employee.id().equals(id));
    }
}
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
}
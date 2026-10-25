
package com.example.employeemanagement;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();

    public InMemoryEmployeeRepository() {
        employees.add(new Employee(1L, "Gopika", "S", "gopika@example.com", "IT"));
        employees.add(new Employee(2L, "Arun", "K", "arun@example.com", "HR"));
        employees.add(new Employee(3L, "Priya", "R", "priya@example.com", "IT"));
    }

    @Override
    public List<Employee> findAll() {
        return employees;
    }

    @Override
    public Employee findById(Long id) {
        return employees.stream()
                .filter(employee -> employee.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        return employees.stream()
                .filter(employee -> employee.department().equalsIgnoreCase(department))
                .toList();
    }

    @Override
    public List<Employee> searchByName(String name) {
        return employees.stream()
                .filter(employee ->
                        employee.firstName().toLowerCase().contains(name.toLowerCase())
                        || employee.lastName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return employees.stream()
                .anyMatch(employee -> employee.email().equalsIgnoreCase(email));
    }

    @Override
    public Employee save(Employee employee) {
        employees.add(employee);
        return employee;
    }

    @Override
    public void update(Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id().equals(updatedEmployee.id())) {
                employees.set(i, updatedEmployee);
                return;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        return employees.removeIf(employee -> employee.id().equals(id));
    }
}

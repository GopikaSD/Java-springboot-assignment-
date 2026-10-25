
package com.example.employeemanagement;

import java.util.List;

public interface EmployeeRepository {

    List<Employee> findAll();

    Employee findById(Long id);

    List<Employee> findByDepartment(String department);

    List<Employee> searchByName(String name);

    boolean existsByEmail(String email);

    Employee save(Employee employee);

    void update(Employee employee);

    boolean deleteById(Long id);
}

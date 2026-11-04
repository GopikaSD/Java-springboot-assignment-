
package com.example.employeemanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public DataSeeder(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) {

        if (employeeRepository.count() > 0) {
            System.out.println("Employees already exist. Skipping seed data.");
            return;
        }

        String[] firstNames = {
            "Gopika", "Arun", "Priya", "Karthik", "Divya",
            "Rahul", "Sneha", "Vijay", "Kavya", "Ajith",
            "Meena", "Suresh", "Nithya", "Surya", "Deepa",
            "Manoj", "Anitha", "Praveen", "Ramya", "Hari",
            "Pavithra", "Dinesh", "Swathi", "Mohan", "Keerthi"
        };

        String[] lastNames = {
            "S", "K", "R", "M", "P",
            "B", "V", "T", "N", "C",
            "D", "A", "G", "J", "L",
            "E", "F", "H", "I", "W",
            "Y", "Z", "Q", "U", "O"
        };

        String[] departmentNames = {
            "IT", "HR", "Finance", "Marketing", "IT"
        };

        List<Department> departments = new ArrayList<>();

        departments.add(new Department("IT", "Coimbatore"));
        departments.add(new Department("HR", "Chennai"));
        departments.add(new Department("Finance", "Bangalore"));
        departments.add(new Department("Marketing", "Chennai"));

        departmentRepository.saveAll(departments);

        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < 25; i++) {

            Employee employee = new Employee(
                null,
                firstNames[i],
                lastNames[i],
                "employee" + (i + 1) + "@example.com",
                "900000" + String.format("%04d", i + 1),
                LocalDate.of(2024, 1, 1).plusDays(i),
                new BigDecimal("25000.00").add(
                    new BigDecimal("1000.00").multiply(
                        BigDecimal.valueOf(i)
                    )
                ),
                String.format("EMP-%04d", i + 1)
            );

            String departmentName =
                departmentNames[i % departmentNames.length];

            Department department = departments.stream()
                .filter(d -> d.getName().equals(departmentName))
                .findFirst()
                .orElseThrow();

            department.addEmployee(employee);

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);

        System.out.println("25 sample employees seeded successfully.");
    }
}

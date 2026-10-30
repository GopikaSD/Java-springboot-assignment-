
package com.example.employeemanagement;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 100)
    private String department;

    @Column(length = 20)
    private String phone;

    private LocalDate joiningDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false, unique = true, length = 50)
    private String employeeCode;

    // Required by JPA
    protected Employee() {
    }

    // Constructor to preserve existing code compatibility
    public Employee(
            Long id,
            String firstName,
            String lastName,
            String email,
            String department,
            String phone,
            LocalDate joiningDate,
            BigDecimal salary,
            String employeeCode) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.employeeCode = employeeCode;
    }

    // JavaBean getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    // Record-style accessors for existing code
    public Long id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String email() {
        return email;
    }

    public String department() {
        return department;
    }

    public String phone() {
        return phone;
    }

    public LocalDate joiningDate() {
        return joiningDate;
    }

    public BigDecimal salary() {
        return salary;
    }

    public String employeeCode() {
        return employeeCode;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
}

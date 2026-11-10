
package com.example.employeemanagement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @Column(length = 20)
    private String phone;

    private LocalDate joiningDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false, unique = true, length = 50)
    private String employeeCode;

    // One-to-One relationship with Address
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    // Many-to-One relationship with Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"employees", "hibernateLazyInitializer", "handler"})
    private Department department;

    // Many-to-Many relationship with Project
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_projects",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    // Required by JPA
    protected Employee() {
    }

    public Employee(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate joiningDate,
            BigDecimal salary,
            String employeeCode) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.employeeCode = employeeCode;
    }

    // Getters
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

    public Address getAddress() {
        return address;
    }

    public Department getDepartment() {
        return department;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    // Record-style accessors
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

    public Address address() {
        return address;
    }

    public Department department() {
        return department;
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

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}

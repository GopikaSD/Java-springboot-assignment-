package com.example.employeemanagement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {

        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    // Create Department
    @Transactional
    public DepartmentResponse createDepartment(
            DepartmentRequest request) {

        Department department = new Department(
                request.name(),
                request.location()
        );

        Department savedDepartment =
                departmentRepository.save(department);

        return toResponse(savedDepartment);
    }

    // Get All Departments with Employees
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        List<Department> departments =
                departmentRepository.findAllWithEmployeesUsingJoinFetch();

        return departments.stream()
                .map(this::toResponse)
                .toList();
    }

    // Get Department by ID
    @Transactional(readOnly = true)
    public Optional<DepartmentResponse> getDepartmentById(Long id) {

        return departmentRepository.findById(id)
                .map(this::toResponse);
    }

    // Get Employees by Department
    @Transactional(readOnly = true)
    public Optional<List<EmployeeResponse>> getEmployeesByDepartment(
            Long departmentId) {

        if (!departmentRepository.existsById(departmentId)) {
            return Optional.empty();
        }

        List<EmployeeResponse> employees =
                employeeRepository.findByDepartment_Id(departmentId)
                        .stream()
                        .map(employeeMapper::toResponse)
                        .toList();

        return Optional.of(employees);
    }

    // Get Top 5 Departments by Employee Count
    @Transactional(readOnly = true)
    public List<DepartmentEmployeeCountProjection>
            getTop5DepartmentsByEmployeeCount() {

        return departmentRepository
                .findTop5DepartmentsByEmployeeCount();
    }

    // Convert Department Entity to Department DTO
    private DepartmentResponse toResponse(Department department) {

        List<EmployeeResponse> employees =
                department.getEmployees()
                        .stream()
                        .map(employeeMapper::toResponse)
                        .toList();

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getLocation(),
                employees
        );
    }
}
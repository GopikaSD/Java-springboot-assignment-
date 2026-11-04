
package com.example.employeemanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final DepartmentRepository departmentRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            DepartmentRepository departmentRepository) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
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

    // Get all employees using database pagination
    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> getAllEmployees(
            int page, int size, String sort) {

        Pageable pageable = createPageable(page, size, sort);

        Page<Employee> employeePage =
                employeeRepository.findAll(pageable);

        List<EmployeeResponse> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }

    // Create pagination settings
    private Pageable createPageable(
            int page, int size, String sort) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        String[] sortParts = sort.split(",");

        if (sortParts.length != 2
                || !sortParts[0].trim().equals("lastName")) {
            throw new IllegalArgumentException(
                    "Sort must use lastName,asc or lastName,desc");
        }

        Sort.Direction direction;

        try {
            direction = Sort.Direction.fromString(
                    sortParts[1].trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Sort direction must be asc or desc");
        }

        return PageRequest.of(
                page, size,
                Sort.by(direction, "lastName"));
    }

    // Find department using its name
    private Department findDepartment(String name) {

        if (name == null || name.isBlank()) {
            return null;
        }

        return departmentRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found: " + name));
    }

    // Get employee by ID
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with ID: " + id));

        return employeeMapper.toResponse(employee);
    }

    // Get employees by department name
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByDepartment(
            String department) {

        return employeeRepository
                .findByDepartment_NameIgnoreCase(department)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    // Search employees by first name or last name
    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> searchEmployeesByName(
            String name, Pageable pageable) {

        String searchName = name == null ? "" : name.trim();

        Page<Employee> employeePage;

        if (searchName.isEmpty()) {
            employeePage = employeeRepository.findAll(pageable);
        } else {
            employeePage = employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            searchName, searchName, pageable);
        }

        List<EmployeeResponse> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }

    // Check whether email exists
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmailIgnoreCase(email);
    }

    // Create employee
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmailIgnoreCase(
                request.email())) {
            throw new DuplicateEmailException(
                    "Email already exists");
        }

        if (employeeRepository.existsByEmployeeCodeIgnoreCase(
                request.employeeCode())) {
            throw new DuplicateEmployeeCodeException(
                    "Employee code already exists");
        }

        Employee employee = employeeMapper.toEntity(request, null);

        employee.setDepartment(findDepartment(request.department()));

        Employee saved = employeeRepository.save(employee);

        logger.info(
                "Employee created successfully with ID: {}",
                saved.id());

        return employeeMapper.toResponse(saved);
    }

    // Update employee
    @Transactional
    public EmployeeResponse updateEmployee(
            Long id, UpdateEmployeeRequest request) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with ID: " + id));

        if (!existing.email().equalsIgnoreCase(request.email())
                && employeeRepository.existsByEmailIgnoreCase(
                        request.email())) {
            throw new DuplicateEmailException(
                    "Email already exists");
        }

        if (!existing.employeeCode().equalsIgnoreCase(
                request.employeeCode())
                && employeeRepository.existsByEmployeeCodeIgnoreCase(
                        request.employeeCode())) {
            throw new DuplicateEmployeeCodeException(
                    "Employee code already exists");
        }

        Employee updated = new Employee(
                existing.id(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.joiningDate(),
                request.salary(),
                request.employeeCode()
        );

        // Preserve existing department if request has no department
        if (request.department() == null
                || request.department().isBlank()) {
            updated.setDepartment(existing.department());
        } else {
            updated.setDepartment(
                    findDepartment(request.department()));
        }

        // Preserve existing address during employee update
        updated.setAddress(existing.address());

        Employee saved = employeeRepository.save(updated);

        logger.info(
                "Employee updated successfully with ID: {}",
                id);

        return employeeMapper.toResponse(saved);
    }

    // Assign employee to a department
    @Transactional
    public EmployeeResponse assignDepartment(
            Long employeeId, Long departmentId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with ID: " + employeeId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found with ID: " + departmentId));

        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);

        logger.info(
                "Department {} assigned to employee {}",
                departmentId,
                employeeId);

        return employeeMapper.toResponse(saved);
    }

    // Delete employee
    @Transactional
    public boolean deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id);
        }

        employeeRepository.deleteById(id);

        logger.info(
                "Employee deleted successfully with ID: {}",
                id);

        return true;
    }
}

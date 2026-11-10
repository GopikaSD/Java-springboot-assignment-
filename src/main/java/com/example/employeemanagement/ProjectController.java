package com.example.employeemanagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeMapper employeeMapper;

    public ProjectController(
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            EmployeeMapper employeeMapper) {

        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.employeeMapper = employeeMapper;
    }

    // Create Project
    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    // Get All Projects
    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Assign Project to Employee
    @PutMapping("/employees/{id}/projects/{projectId}")
    @Transactional
    public EmployeeResponse assignProject(
            @PathVariable Long id,
            @PathVariable Long projectId) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        employee.getProjects().add(project);
        project.getEmployees().add(employee);

        employeeRepository.save(employee);

        return employeeMapper.toResponse(employee);
    }

    // Unassign Project from Employee
    @DeleteMapping("/employees/{id}/projects/{projectId}")
    @Transactional
    public void unassignProject(
            @PathVariable Long id,
            @PathVariable Long projectId) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        employee.getProjects().remove(project);
        project.getEmployees().remove(employee);

        employeeRepository.save(employee);
    }

    // Get Employees by Project ID
    @GetMapping("/projects/{id}/employees")
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getProjectEmployees(
            @PathVariable Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        return project.getEmployees()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}
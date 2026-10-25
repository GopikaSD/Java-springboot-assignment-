package com.example.employeemanagement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(
        target = "fullName",
        expression = "java(employee.firstName() + \" \" + employee.lastName())"
    )
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "id", source = "id")
    Employee toEntity(EmployeeRequest request, Long id);
}
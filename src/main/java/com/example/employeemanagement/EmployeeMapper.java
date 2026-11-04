
package com.example.employeemanagement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(
            target = "fullName",
            expression = "java(employee.firstName() + \" \" + employee.lastName())"
    )
    @Mapping(
            target = "department",
            expression = "java(employee.department() != null ? employee.department().getName() : null)"
    )
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "request.address")
    @Mapping(target = "department", ignore = true)
    Employee toEntity(EmployeeRequest request, Long id);

    default Address toEntity(AddressRequest request) {
        if (request == null) {
            return null;
        }

        return new Address(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode()
        );
    }
}

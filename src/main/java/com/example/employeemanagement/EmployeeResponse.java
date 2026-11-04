
package com.example.employeemanagement;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        String phone,
        LocalDate joiningDate,
        BigDecimal salary,
        String employeeCode,
        String fullName,
        AddressResponse address
) {
}

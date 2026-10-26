
package com.example.employeemanagement;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmployeeCodeValidator
        implements ConstraintValidator<ValidEmployeeCode, String> {

    @Override
    public boolean isValid(
            String employeeCode,
            ConstraintValidatorContext context) {

        if (employeeCode == null) {
            return true;
        }

        return employeeCode.matches("EMP-[0-9]{4}");
    }
}

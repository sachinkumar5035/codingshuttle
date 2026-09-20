package com.example.demo.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmployeeAgeValidator implements ConstraintValidator<EmployeeAgeValidation,Integer> {
    @Override
    public boolean isValid(Integer inputAge, ConstraintValidatorContext constraintValidatorContext) {
        if (inputAge == null) {
            return true;
        }
        return inputAge >= 18 && inputAge <= 80;
    }
}

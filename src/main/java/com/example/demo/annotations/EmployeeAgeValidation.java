package com.example.demo.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER}) // This annotation can be applied to fields and parameters
@Constraint(validatedBy = {EmployeeAgeValidator.class}) // Specify the validator class that will handle the validation logic
public @interface EmployeeAgeValidation {

    String message() default "Age of Employee must be between 18 and 80";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

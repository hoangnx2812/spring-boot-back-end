package com.devteria.identityservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirthConstraint, LocalDate> {

    private int min;

    @Override
    public void initialize(DateOfBirthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        long year = ChronoUnit.YEARS.between(value, LocalDate.now());
        return year >= min;
    }

}
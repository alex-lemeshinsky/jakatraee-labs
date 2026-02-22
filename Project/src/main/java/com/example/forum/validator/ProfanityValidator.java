package com.example.forum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProfanityValidator implements ConstraintValidator<NoProfanity, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.toLowerCase().contains("spam");
    }
}

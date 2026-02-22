package com.example.forum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProfanityValidator.class)
public @interface NoProfanity {
    String message() default "Текст містить заборонені слова";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

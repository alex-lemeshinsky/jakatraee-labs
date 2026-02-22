package com.example.forum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TopicNotClosedValidator.class)
public @interface TopicNotClosed {
    String message() default "Ви не можете додавати дописи до закритої теми";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

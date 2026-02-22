package com.example.forum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TopicObjectValidator.class)
public @interface ValidTopic {
    String message() default "Заголовок та опис не можуть бути однаковими";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

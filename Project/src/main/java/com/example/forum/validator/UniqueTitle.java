package com.example.forum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTitleValidator.class)
public @interface UniqueTitle {
    String message() default "Тема з таким заголовком вже існує";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

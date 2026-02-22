package com.example.forum.validator;

import com.example.forum.dto.TopicDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TopicObjectValidator implements ConstraintValidator<ValidTopic, TopicDTO> {

    @Override
    public boolean isValid(TopicDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;
        if (dto.title == null || dto.description == null) return true;
        return !dto.title.equalsIgnoreCase(dto.description);
    }
}

package com.example.forum.validator;

import com.example.forum.model.Topic;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TopicObjectValidator implements ConstraintValidator<ValidTopic, Topic> {
    @Override
    public boolean isValid(Topic topic, ConstraintValidatorContext context) {
        if (topic.getTitle() == null || topic.getDescription() == null) return true;
        return !topic.getTitle().equalsIgnoreCase(topic.getDescription());
    }
}

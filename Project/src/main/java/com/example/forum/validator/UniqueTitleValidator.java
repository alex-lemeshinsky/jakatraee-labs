package com.example.forum.validator;

import com.example.forum.service.TopicService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Inject
    private TopicService topicService;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null || title.isBlank() || topicService == null) {
            return true;
        }
        return topicService.getAllTopics().stream()
                .noneMatch(t -> t.getTitle().trim().equalsIgnoreCase(title.trim()));
    }
}

package com.example.forum.validator;

import com.example.forum.dto.PostDTO;
import com.example.forum.model.Topic;
import com.example.forum.service.TopicService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TopicNotClosedValidator implements ConstraintValidator<TopicNotClosed, PostDTO> {
    @Inject
    private TopicService topicService;

    @Override
    public boolean isValid(PostDTO dto, ConstraintValidatorContext context) {
        if (dto.topicId == null) return true;
        Topic topic = topicService.getTopicById(dto.topicId);
        return topic != null && !topic.isClosed();
    }
}
package com.example.forum.dto;

import com.example.forum.validator.NoProfanity;
import com.example.forum.validator.TopicNotClosed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@TopicNotClosed
public class PostDTO {
    @NoProfanity
    @NotBlank(message = "Допис не може бути порожнім")
    @Size(min = 2, max = 500)
    public String content;

    @NotNull(message = "ID теми обов'язкове")
    public Long topicId;

    public String authorName;
}
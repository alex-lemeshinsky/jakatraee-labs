package com.example.forum.dto;


import com.example.forum.validator.NoProfanity;
import com.example.forum.validator.UniqueTitle;
import com.example.forum.validator.ValidTopic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@ValidTopic
public class TopicDTO {

    @NotBlank(message = "Заголовок не може бути порожнім")
    @Size(min = 5, max = 100)
    @UniqueTitle
    public String title;

    @NoProfanity
    public String description;

    public boolean closed;
}
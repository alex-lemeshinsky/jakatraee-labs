package com.example.forum.model;

import jakarta.json.bind.annotation.JsonbDateFormat;

import java.util.Date;

public class Post {
    private Long id;
    private String content;
    private User author;
    private Long topicId;

    @JsonbDateFormat("dd.MM.yyyy HH:mm")
    private Date createdAt;

    @JsonbDateFormat("dd.MM.yyyy HH:mm")
    private Date updatedAt;

    public Post(Long id, String content, User author, Long topicId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.topicId = topicId;
        this.createdAt = new Date();
        this.updatedAt = null;
    }

    public Post() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
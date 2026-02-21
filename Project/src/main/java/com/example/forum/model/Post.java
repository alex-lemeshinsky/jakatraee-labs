package com.example.forum.model;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String content;
    private User author;
    private Long topicId;
    private LocalDateTime createdAt;

    public Post(Long id, String content, User author, Long topicId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.topicId = topicId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Long getTopicId() { return topicId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(long id) { this.id = id; }

    public void setCreatedAt(LocalDateTime now) { this.createdAt = now; }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "—";
        }
        return createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"));
    }
}
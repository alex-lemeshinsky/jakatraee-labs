package com.example.forum.model;

import java.time.LocalDateTime;
import java.util.List;

public class Topic {
    private Long id;
    private String title;
    private String description;
    private boolean closed;
    private LocalDateTime createdAt;

    private List<Post> posts;

    public Topic(Long id, String title, String description, boolean closed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.closed = closed;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isClosed() { return closed; }
    public List<Post> getPosts() { return posts; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setPosts(List<Post> posts) { this.posts = posts; }

    public void setId(long id) { this.id = id; }

    public void setCreatedAt(LocalDateTime now) { this.createdAt = now; }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "—";
        }
        return createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"));
    }
}

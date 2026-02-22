package com.example.forum.model;

import jakarta.json.bind.annotation.JsonbDateFormat;

import java.util.Date;
import java.util.List;

public class Topic {
    private Long id;
    private String title;
    private String description;
    private boolean closed;

    @JsonbDateFormat("dd.MM.yyyy HH:mm")
    private Date createdAt;

    private List<Post> posts;

    public Topic() {}

    public Topic(Long id, String title, String description, boolean closed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.closed = closed;
        this.createdAt = new Date();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isClosed() { return closed; }
    public List<Post> getPosts() { return posts; }
    public Date getCreatedAt() { return createdAt; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

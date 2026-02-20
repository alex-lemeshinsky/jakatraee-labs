package com.example.forum.service;

import com.example.forum.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByTopicId(Long topicId);
    void createPost(Post post);
}

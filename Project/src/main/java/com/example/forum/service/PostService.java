package com.example.forum.service;

import com.example.forum.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByTopicId(Long topicId);

    List<Post> getFilteredPosts(Long topicId, int page, int size);

    void createPost(Post post);

    void updatePost(Post existing);

    Post getPostById(Long id);

    void deletePost(Long id);
}

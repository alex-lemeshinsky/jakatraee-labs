package com.example.forum.dao;

import com.example.forum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDAO {
    void save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAllByTopicId(Long topicId);
    void update(Post post);
    void delete(Long id);
    List<Post> findFiltered(Long topicId, int page, int size);
}

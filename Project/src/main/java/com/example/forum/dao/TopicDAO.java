package com.example.forum.dao;

import com.example.forum.model.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicDAO {
    Long save(Topic topic);
    Optional<Topic> findById(Long id);
    List<Topic> findAll();
    void update(Topic topic);
    void delete(Long id);
    List<Topic> findByTitle(String title);
    List<Topic> findFiltered(String search, int page, int size);
}
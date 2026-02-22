package com.example.forum.service;

import com.example.forum.model.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getAllTopics();
    Topic getTopicById(Long id);
    void createTopic(Topic topic);
    void updateTopic(Topic topic);
    void deleteTopic(Long id);
    List<Topic> getFilteredTopics(String search, int page, int size);
}

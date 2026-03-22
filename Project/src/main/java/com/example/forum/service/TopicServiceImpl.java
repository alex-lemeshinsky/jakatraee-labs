package com.example.forum.service;

import com.example.forum.dao.TopicDAO;
import com.example.forum.model.Topic;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.List;

@Singleton
@Startup
@Local(TopicService.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TopicServiceImpl implements TopicService {

    @EJB
    private PostService postService;

    @EJB
    private TopicDAO topicDAO;


    @Override
    public List<Topic> getAllTopics() {
        return topicDAO.findAll();
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicDAO.findById(id).orElse(null);
    }

    @Override
    public void createTopic(Topic topic) {
        topicDAO.save(topic);
    }

    @Override
    public void updateTopic(Topic topic) {
        topicDAO.update(topic);
    }

    @Override
    public void deleteTopic(Long id) {
        topicDAO.delete(id);
    }

    @Override
    public List<Topic> getFilteredTopics(String search, int page, int size) {
        return topicDAO.findFiltered(search, page, size);
    }
}
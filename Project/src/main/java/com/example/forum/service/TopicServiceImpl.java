package com.example.forum.service;

import com.example.forum.dao.TopicDAO;
import com.example.forum.model.Topic;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.util.List;

@Stateless
@Local(TopicService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TopicServiceImpl implements TopicService {

    @EJB
    private PostModerationService postModerationService;

    @EJB
    private TopicDAO topicDAO;


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Topic> getAllTopics() {
        return topicDAO.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
    public Topic closeTopic(Long id, boolean simulateFailure) {
        Topic topic = topicDAO.findById(id)
                .orElseThrow(() -> new TopicClosureException("Тему не знайдено."));

        if (topic.isClosed()) {
            throw new TopicClosureException("Тема вже закрита.");
        }

        topic.setClosed(true);
        topicDAO.update(topic);
        postModerationService.markTopicPostsAsClosed(id, simulateFailure);

        return topic;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Topic> getFilteredTopics(String search, int page, int size) {
        return topicDAO.findFiltered(search, page, size);
    }
}

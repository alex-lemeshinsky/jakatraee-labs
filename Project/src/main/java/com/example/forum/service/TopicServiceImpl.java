package com.example.forum.service;

import com.example.forum.model.Topic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TopicServiceImpl implements TopicService {
    private List<Topic> topics = new ArrayList<>();

    public TopicServiceImpl() { // при ініціалізації додаєм тестові теми
        topics.add(new Topic(1L, "Тема 1", "Опис теми 1", false));
        topics.add(new Topic(2L, "Тема 2", "Опис теми 2", true));
        topics.add(new Topic(3L, "Тема 3", "Опис теми 3", true));
        topics.add(new Topic(4L, "Тема 4", "Опис теми 4", false));
    }

    @Override
    public List<Topic> getAllTopics() {
        return new ArrayList<>(topics);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topics.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void createTopic(Topic topic) {
        topic.setId(topics.size() + 1);
        topics.add(topic);
    }

    @Override
    public void updateTopic(Topic topic) {
        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i).getId().equals(topic.getId())) {
                topics.set(i, topic);
                break;
            }
        }
    }

    @Override
    public void deleteTopic(Long id) {
        topics.removeIf(t -> t.getId().equals(id));
    }
}

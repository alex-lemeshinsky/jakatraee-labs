package com.example.forum.service;

import com.example.forum.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicServiceImpl implements TopicService {
    private List<Topic> topics = new ArrayList<>();

    public TopicServiceImpl() { // при ініціалізації додаєм тестові теми
        topics.add(new Topic(1L, "Як правильно готувати каву вдома, щоб була як в кав'ярні?", "Ділимося секретами, рецептами, сортами зерен, кавоварками та помилками, яких варто уникати. Від еспресо до альтернативних методів — усе про каву без фільтрів.", false));
        topics.add(new Topic(2L, "Як ви економите на продуктах у 2026 році?", "Список магазинів, акції, додатки, рецепти з дешевих продуктів, чи варто купувати оптом, де брати знижки. Реальні лайфхаки, а не теорія.", false));
        topics.add(new Topic(3L, "Які подкасти вас рятують у транспорті / на роботі?", "Українські, російськомовні, англомовні — все. Теми, ведучі, скільки слухаєте, чому саме вони. Рекомендації з посиланнями.", true));
        topics.add(new Topic(4L, "Найгірші подарунки, які ви коли-небудь отримували (і що з ними зробили)", "Розкажіть смішні/страшні історії. Від «подарункового набору для риболовлі, коли я боюся води» до «сертифікату на стрибок з парашутом». Без образ.", false));
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

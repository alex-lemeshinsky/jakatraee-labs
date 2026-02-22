package com.example.forum.service;

import com.example.forum.model.Topic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class TopicServiceImpl implements TopicService {

    @Inject
    private PostService postService;

    private List<Topic> topics = new ArrayList<>();

    private final AtomicLong idCounter = new AtomicLong(13L);

    public TopicServiceImpl() {
        topics.add(new Topic(1L, "Як правильно готувати каву вдома, щоб була як в кав'ярні?", "Ділимося секретами, рецептами, сортами зерен, кавоварками та помилками, яких варто уникати. Від еспресо до альтернативних методів — усе про каву без фільтрів.", false));
        topics.add(new Topic(2L, "Як ви економите на продуктах у 2026 році?", "Список магазинів, акції, додатки, рецепти з дешевих продуктів, чи варто купувати оптом, де брати знижки. Реальні лайфхаки, а не теорія.", false));
        topics.add(new Topic(3L, "Які подкасти вас рятують у транспорті / на роботі?", "Українські, російськомовні, англомовні — все. Теми, ведучі, скільки слухаєте, чому саме вони. Рекомендації з посиланнями.", true));
        topics.add(new Topic(4L, "Найгірші подарунки, які ви коли-небудь отримували (і що з ними зробили)", "Розкажіть смішні/страшні історії. Від «подарункового набору для риболовлі, коли я боюся води» до «сертифікату на стрибок з парашутом». Без образ.", false));
        topics.add(new Topic(5L, "Java 25: що нового та чи варто оновлюватися?", "Обговорюємо останні фічі мови, продуктивність та зміни в стандартній бібліотеці.", false));
        topics.add(new Topic(6L, "Найкращі серіали від Netflix за останній рік", "Що подивитися ввечері? Рейтинги, відгуки без спойлерів, рекомендації до перегляду.", false));
        topics.add(new Topic(7L, "Віддалена робота: як облаштувати робоче місце вдома?", "Монітори, крісла, освітлення та тайм-менеджмент. Як не зійти з розуму в чотирьох стінах.", false));
        topics.add(new Topic(8L, "Куди поїхати у відпустку взимку, якщо не на лижі?", "Теплі країни, цікаві міста Європи, бюджетні варіанти та екзотика.", false));
        topics.add(new Topic(9L, "Чи замінить штучний інтелект програмістів до 2030 року?", "Прогнози, реальність, вплив AI на ринок праці та розвиток інструментів розробки.", false));
        topics.add(new Topic(10L, "Спорт вдома: мінімальний набір інвентарю для результату", "Гантелі, килимки, резинки. Чи реально накачати м'язи без походу в спортзал?", false));
        topics.add(new Topic(11L, "Книги, які змінили ваш світогляд", "Поділіться літературою, яка залишила глибокий слід. Психологія, філософія, художня література.", false));
        topics.add(new Topic(12L, "Електромобілі в Україні: реалії володіння у 2026 році", "Зарядні станції, запас ходу взимку, вартість обслуговування та порівняння з авто на бензині.", false));
        topics.add(new Topic(13L, "Як почати вчити нову іноземну мову самостійно?", "Методики, додатки типу Duolingo, перегляд фільмів в оригіналі та розмовні клуби.", false));
    }

    @Override
    public List<Topic> getAllTopics() {
        for (Topic topic : topics) {
            topic.setPosts(postService.getPostsByTopicId(topic.getId()));
        }
        return topics;
    }

    @Override
    public Topic getTopicById(Long id) {
        return topics.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void createTopic(Topic topic) {
        topic.setId(idCounter.incrementAndGet());
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

    @Override
    public List<Topic> getFilteredTopics(String search, int page, int size) {
        return topics.stream()
                .filter(t -> search == null || search.isEmpty() ||
                        t.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                        t.getDescription().toLowerCase().contains(search.toLowerCase()))

                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
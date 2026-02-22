package com.example.forum.service;

import com.example.forum.model.Post;
import com.example.forum.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostServiceImpl implements PostService {
    private List<Post> posts = new ArrayList<>();

    private final AtomicLong idCounter = new AtomicLong(0);

    public PostServiceImpl() {
        posts.add(new Post(1L, """
                Я просто роблю лате/капучино на френч-пресі + молочна пінка з френч-пресу (так, це реально працює).
                Рецепт на 1 велику чашку:
                
                15 г кави (середній помел)
                250 мл води 93 °C
                настояти 4 хвилини, повільно натиснути поршень
                паралельно грію 150 мл молока в мікрохвильовці до 60–65 °C
                збиваю молоко тим самим френч-пресом (швидко 20–30 разів поршнем вгору-вниз) — виходить густа пінка
                
                Заливаю каву в чашку, зверху акуратно виливаю пінку ложкою. Якщо додати трохи кориці або ванілі — взагалі як в кав’ярні.
                Плюси: майже нуль брудного посуду, дешево, швидко (5 хвилин). Мінуси: не справжній еспресо, але для ранку з дітьми — ідеально.
                Хто пробував — як вам? Чи є кращий спосіб без дорогих девайсів?""", new User("Kavoman_88", "user"), 1L));
        posts.add(new Post(2L, """
                Бери за основу правило 1:8–1:9 (кава : вода за вагою), це вже 80% успіху.
                Мій робочий рецепт еспресо-подібної кави на звичайній турці/гейзері (бо в мене немає нормальної машини):
                
                18–20 г свіжозмеленої кави (середній помел, трохи дрібніше ніж для френч-пресу)
                180–200 мл води
                турка на середній вогонь, помішувати до першого підйому піни
                зняти з вогню, дати піну осісти, повторити 2 рази
                дати постояти 30–40 секунд, наливати повільно, щоб осад залишився
                
                Головні лайфхаки:
                
                кава свіжа (не старше 3 тижнів після обсмажування)
                вода 92–96 °C (не кип’яток!)
                помел під свій посуд — якщо гірчить → грубіший, якщо кисло → дрібніший
                купуй зерно в пакетах з клапаном і мели безпосередньо перед варінням
                
                В мене вже пів року виходить стабільно краще, ніж в 70% київських кав’ярень. Спробуйте — не пошкодуєте.""", new User("Latteman_22", "user"), 1L));
    }

    @Override
    public List<Post> getPostsByTopicId(Long topicId) {
        if (topicId == null) {
            return new ArrayList<>();
        }

        return posts.stream()
                .filter(post -> topicId.equals(post.getTopicId()))
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getFilteredPosts(Long topicId, int page, int size) {
        return posts.stream()
                .filter(p -> p.getTopicId().equals(topicId))
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public void createPost(Post post) {
        post.setId(idCounter.incrementAndGet());
        posts.add(post);
    }

    @Override
    public void updatePost(Post updatedPost) {
        if (updatedPost == null || updatedPost.getId() == null) {
            return;
        }

        for (Post p : posts) {
            if (p.getId().equals(updatedPost.getId())) {
                p.setContent(updatedPost.getContent());
                p.setUpdatedAt(new Date());
                return;
            }
        }
    }

    @Override
    public Post getPostById(Long id) {
        return posts.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deletePost(Long id) {
        posts.removeIf(t -> t.getId().equals(id));
    }
}

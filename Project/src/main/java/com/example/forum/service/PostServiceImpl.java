package com.example.forum.service;

import com.example.forum.model.Post;
import com.example.forum.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    private List<Post> posts = new ArrayList<>();

    public PostServiceImpl() { // при ініціалізації додаєм тестові пости
        posts.add(new Post(1L, "Допис 1 в темі 1", new User("user1", "user"), 1L));
        posts.add(new Post(2L, "Допис 2 в темі 1", new User("user2", "user"), 1L));

        posts.add(new Post(3L, "Допис 1 в темі 2", new User("user2", "user"), 2L));
    }

    @Override
    public List<Post> getPostsByTopicId(Long topicId) {
        List<Post> result = new ArrayList<>();
        for (Post p : posts) {
            if (p.getTopicId().equals(topicId)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public void createPost(Post post) {
        post.setId(posts.size() + 1);
        posts.add(post);
    }
}

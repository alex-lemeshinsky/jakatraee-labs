package com.example.forum.dao;

import com.example.forum.model.Post;
import com.example.forum.model.Topic;
import com.example.forum.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class JpaPostDAO implements PostDAO {

    @PersistenceContext(unitName = "ForumPU")
    private EntityManager em;

    @Override
    public void save(Post post) {
        if (post.getTopicId() != null) {
            post.setTopic(em.getReference(Topic.class, post.getTopicId()));
        }
        if (post.getAuthor() != null && post.getAuthor().getUsername() != null) {
            User managedUser = em.find(User.class, post.getAuthor().getUsername());
            if (managedUser != null) {
                post.setAuthor(managedUser);
            }
        }
        em.persist(post);
        em.flush();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    @Override
    public List<Post> findAllByTopicId(Long topicId) {
        return em.createQuery(
                "SELECT p FROM Post p WHERE p.topic.id = :topicId ORDER BY p.createdAt", Post.class)
                .setParameter("topicId", topicId)
                .getResultList();
    }

    @Override
    public void update(Post post) {
        Post managed = em.find(Post.class, post.getId());
        if (managed != null) {
            managed.setContent(post.getContent());
            managed.setUpdatedAt(new Date());
        }
    }

    @Override
    public void delete(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

    @Override
    public List<Post> findFiltered(Long topicId, int page, int size) {
        return em.createQuery(
                "SELECT p FROM Post p WHERE p.topic.id = :topicId ORDER BY p.createdAt DESC", Post.class)
                .setParameter("topicId", topicId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }
}

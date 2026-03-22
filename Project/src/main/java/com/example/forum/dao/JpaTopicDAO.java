package com.example.forum.dao;

import com.example.forum.model.Topic;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class JpaTopicDAO implements TopicDAO {

    @PersistenceContext(unitName = "ForumPU")
    private EntityManager em;

    @Override
    public void save(Topic topic) {
        em.persist(topic);
        em.flush();
    }

    @Override
    public Optional<Topic> findById(Long id) {
        return Optional.ofNullable(em.find(Topic.class, id));
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = em.createQuery(
                "SELECT t FROM Topic t ORDER BY t.createdAt DESC", Topic.class)
                .getResultList();
        for (Topic t : topics) {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Post p WHERE p.topic.id = :topicId", Long.class)
                    .setParameter("topicId", t.getId())
                    .getSingleResult();
            t.setPostCount(count.intValue());
        }
        return topics;
    }

    @Override
    public void update(Topic topic) {
        Topic managed = em.find(Topic.class, topic.getId());
        if (managed != null) {
            managed.setTitle(topic.getTitle());
            managed.setDescription(topic.getDescription());
            managed.setClosed(topic.isClosed());
        }
    }

    @Override
    public void delete(Long id) {
        Topic topic = em.find(Topic.class, id);
        if (topic != null) {
            em.remove(topic);
        }
    }

    @Override
    public List<Topic> findByTitle(String titleQuery) {
        return em.createQuery(
                "SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(:pattern)", Topic.class)
                .setParameter("pattern", "%" + titleQuery + "%")
                .getResultList();
    }

    @Override
    public List<Topic> findFiltered(String search, int page, int size) {
        TypedQuery<Topic> query;
        if (search == null || search.isEmpty()) {
            query = em.createQuery(
                    "SELECT t FROM Topic t ORDER BY t.createdAt DESC", Topic.class);
        } else {
            query = em.createQuery(
                    "SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(:pattern) " +
                    "OR LOWER(t.description) LIKE LOWER(:pattern) ORDER BY t.createdAt DESC", Topic.class);
            query.setParameter("pattern", "%" + search + "%");
        }
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
}

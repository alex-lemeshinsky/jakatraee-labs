package com.example.forum.dao;

import com.example.forum.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class JpaUserDAO implements UserDAO {

    @PersistenceContext(unitName = "ForumPU")
    private EntityManager em;

    @Override
    public Optional<User> authenticate(String identifier, String password) {
        List<User> results = em.createQuery(
                "SELECT u FROM User u WHERE (u.username = :id OR u.email = :id) AND u.password = :pw", User.class)
                .setParameter("id", identifier)
                .setParameter("pw", password)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean exists(String username) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :val OR u.email = :val", Long.class)
                .setParameter("val", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }
}

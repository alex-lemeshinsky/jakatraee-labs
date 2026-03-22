package com.example.forum.dao;

import com.example.forum.model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> authenticate(String username, String password);
    boolean exists(String username);
    void save(User user);
}

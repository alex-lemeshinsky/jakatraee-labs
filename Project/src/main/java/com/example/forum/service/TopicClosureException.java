package com.example.forum.service;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TopicClosureException extends RuntimeException {
    public TopicClosureException(String message) {
        super(message);
    }
}

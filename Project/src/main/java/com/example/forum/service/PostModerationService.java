package com.example.forum.service;

public interface PostModerationService {
    int markTopicPostsAsClosed(Long topicId, boolean simulateFailure);
}

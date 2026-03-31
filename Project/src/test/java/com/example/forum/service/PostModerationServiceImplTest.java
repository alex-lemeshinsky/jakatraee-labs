package com.example.forum.service;

import com.example.forum.dao.PostDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostModerationServiceImplTest {

    @Mock
    private PostDAO postDAO;

    @InjectMocks
    private PostModerationServiceImpl postModerationService;

    @Test
    void markTopicPostsAsClosedShouldUpdatePosts() {
        when(postDAO.appendClosureNoticeToTopicPosts(eq(7L), contains("[SYSTEM]"), any(Date.class)))
                .thenReturn(3);

        int updated = postModerationService.markTopicPostsAsClosed(7L, false);

        assertEquals(3, updated);
        verify(postDAO).appendClosureNoticeToTopicPosts(eq(7L), contains("[SYSTEM]"), any(Date.class));
    }

    @Test
    void markTopicPostsAsClosedShouldThrowWhenFailureSimulationEnabled() {
        when(postDAO.appendClosureNoticeToTopicPosts(eq(7L), contains("[SYSTEM]"), any(Date.class)))
                .thenReturn(3);

        assertThrows(TopicClosureException.class, () -> postModerationService.markTopicPostsAsClosed(7L, true));

        verify(postDAO).appendClosureNoticeToTopicPosts(eq(7L), contains("[SYSTEM]"), any(Date.class));
    }
}

package com.example.forum.service;

import com.example.forum.dao.TopicDAO;
import com.example.forum.model.Topic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    private TopicDAO topicDAO;

    @Mock
    private PostModerationService postModerationService;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    void closeTopicShouldUpdateTopicAndDelegatePostModeration() {
        Topic topic = new Topic(10L, "Topic", "Desc", false);
        when(topicDAO.findById(10L)).thenReturn(Optional.of(topic));

        Topic result = topicService.closeTopic(10L, false);

        assertSame(topic, result);
        assertTrue(result.isClosed());

        var inOrder = inOrder(topicDAO, postModerationService);
        inOrder.verify(topicDAO).findById(10L);
        inOrder.verify(topicDAO).update(topic);
        inOrder.verify(postModerationService).markTopicPostsAsClosed(10L, false);
    }

    @Test
    void closeTopicShouldPropagateFailureFromNestedService() {
        Topic topic = new Topic(12L, "Topic", "Desc", false);
        when(topicDAO.findById(12L)).thenReturn(Optional.of(topic));
        doThrow(new TopicClosureException("rollback"))
                .when(postModerationService).markTopicPostsAsClosed(12L, true);

        TopicClosureException exception = assertThrows(
                TopicClosureException.class,
                () -> topicService.closeTopic(12L, true)
        );

        assertSame("rollback", exception.getMessage());

        var inOrder = inOrder(topicDAO, postModerationService);
        inOrder.verify(topicDAO).findById(12L);
        inOrder.verify(topicDAO).update(topic);
        inOrder.verify(postModerationService).markTopicPostsAsClosed(12L, true);
    }

    @Test
    void closeTopicShouldFailWhenTopicDoesNotExist() {
        when(topicDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TopicClosureException.class, () -> topicService.closeTopic(99L, false));

        verify(topicDAO, never()).update(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(postModerationService);
    }
}

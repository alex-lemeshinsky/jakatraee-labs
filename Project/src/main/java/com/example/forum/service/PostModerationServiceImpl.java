package com.example.forum.service;

import com.example.forum.dao.PostDAO;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.util.Date;

@Stateless
@Local(PostModerationService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PostModerationServiceImpl implements PostModerationService {

    private static final String CLOSURE_NOTICE =
            "\n\n[SYSTEM] Тему закрито. Нові відповіді вимкнено модератором.";

    @EJB
    private PostDAO postDAO;

    @Override
    public int markTopicPostsAsClosed(Long topicId, boolean simulateFailure) {
        int updatedRows = postDAO.appendClosureNoticeToTopicPosts(topicId, CLOSURE_NOTICE, new Date());
        if (simulateFailure) {
            throw new TopicClosureException("Симуляція помилки: оновлення дописів завершилося збоєм, транзакцію відкотили.");
        }
        return updatedRows;
    }
}

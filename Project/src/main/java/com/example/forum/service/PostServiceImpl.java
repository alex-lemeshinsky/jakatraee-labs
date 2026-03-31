package com.example.forum.service;

import com.example.forum.dao.PostDAO;
import com.example.forum.model.Post;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.util.List;

@Stateless
@Local(PostService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PostServiceImpl implements PostService {

    @EJB
    private PostDAO postDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Post> getPostsByTopicId(Long topicId) {
        return postDAO.findAllByTopicId(topicId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Post> getFilteredPosts(Long topicId, int page, int size) {
        return postDAO.findFiltered(topicId, page, size);
    }

    @Override
    public void createPost(Post post) {
        postDAO.save(post);
    }

    @Override
    public void updatePost(Post updatedPost) {
        postDAO.update(updatedPost);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Post getPostById(Long id) {
        return postDAO.findById(id).orElse(null);
    }

    @Override
    public void deletePost(Long id) {
        postDAO.delete(id);
    }
}

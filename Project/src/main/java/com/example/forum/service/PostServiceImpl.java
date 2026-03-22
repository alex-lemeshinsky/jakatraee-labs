package com.example.forum.service;

import com.example.forum.dao.PostDAO;
import com.example.forum.model.Post;
import jakarta.ejb.*;

import java.util.List;

@Singleton
@Startup
@Local(PostService.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class PostServiceImpl implements PostService {

    @EJB
    private PostDAO postDAO;

    @Override
    public List<Post> getPostsByTopicId(Long topicId) {
        return postDAO.findAllByTopicId(topicId);
    }

    @Override
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
    public Post getPostById(Long id) {
        return postDAO.findById(id).orElse(null);
    }

    @Override
    public void deletePost(Long id) {
        postDAO.delete(id);
    }
}
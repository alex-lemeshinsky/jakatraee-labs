package com.example.forum.controller;

import com.example.forum.model.Post;
import com.example.forum.model.Topic;
import com.example.forum.model.User;
import com.example.forum.service.PostService;
import com.example.forum.service.PostServiceImpl;
import com.example.forum.service.TopicService;
import com.example.forum.service.TopicServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    private PostService postService = new PostServiceImpl();
    private TopicService topicService = new TopicServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long topicId = Long.parseLong(req.getParameter("topicId"));
        Topic topic = topicService.getTopicById(topicId);
        topic.setPosts(postService.getPostsByTopicId(topicId));
        req.setAttribute("topic", topic);
        req.getRequestDispatcher("/posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && ("user".equals(user.getRole()) || "admin".equals(user.getRole()))) {
            Long topicId = Long.parseLong(req.getParameter("topicId"));
            String content = req.getParameter("content");
            Post post = new Post(null, content, user, topicId);
            postService.createPost(post);
        }
        resp.sendRedirect("/posts?topicId=" + req.getParameter("topicId"));
    }
}

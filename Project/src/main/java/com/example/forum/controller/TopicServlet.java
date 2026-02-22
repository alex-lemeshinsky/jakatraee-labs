package com.example.forum.controller;

import com.example.forum.model.Topic;
import com.example.forum.model.User;
import com.example.forum.service.TopicService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/topics")
public class TopicServlet extends HttpServlet {

    @Inject
    private TopicService topicService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Topic> topics = topicService.getAllTopics();
        req.setAttribute("topics", topics);
        req.getRequestDispatcher("/topics.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole())) {
            String action = req.getParameter("action");
            if ("create".equals(action)) {
                String title = req.getParameter("title");
                String desc = req.getParameter("description");
                Topic topic = new Topic(null, title, desc, false);
                topicService.createTopic(topic);
            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                String title = req.getParameter("title");
                String desc = req.getParameter("description");
                Topic topic = new Topic(id, title, desc, false); // Приклад
                topicService.updateTopic(topic);
            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                topicService.deleteTopic(id);
            }
        }
        resp.sendRedirect("/topics");
    }
}

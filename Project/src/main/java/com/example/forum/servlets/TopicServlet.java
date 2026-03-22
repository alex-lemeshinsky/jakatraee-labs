package com.example.forum.servlets;

import com.example.forum.dao.TopicDAO;
import com.example.forum.model.Topic;
import com.example.forum.model.User;
import com.example.forum.service.TopicService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet({"/topics", "/topics/edit"})
public class TopicServlet extends HttpServlet {

    @EJB
    private TopicService topicService;

    @EJB
    private TopicDAO topicDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/topics/edit".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Long id = Long.parseLong(idParam);
                Optional<Topic> topicOptional = topicDAO.findById(id);

                if (topicOptional.isPresent()) {
                    Topic topic = topicOptional.get();
                    req.setAttribute("topic", topic);
                    req.getRequestDispatcher("/WEB-INF/views/editTopic.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Тему не знайдено");
                }
                return;
            }
        }

        List<Topic> topics = topicService.getAllTopics();
        req.setAttribute("topics", topics);
        req.getRequestDispatcher("/WEB-INF/views/topics.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/topics");
            return;
        }
        String action = req.getParameter("action");
        String topicIdStr = req.getParameter("id");
        try {
            if ("create".equals(action)) {
                String title = req.getParameter("title");
                String desc = req.getParameter("description");
                Topic topic = new Topic(null, title, desc, false);
                topicService.createTopic(topic);

            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                String closedParam = req.getParameter("closed");
                boolean isClosed = "true".equals(closedParam);
                if (title != null && !title.trim().isEmpty()) {
                    Topic topic = new Topic();
                    topic.setId(id);
                    topic.setTitle(title);
                    topic.setDescription(description);
                    topic.setClosed(isClosed);
                    topicDAO.update(topic);
                }
                resp.sendRedirect(req.getContextPath() + "/topics");
                return;
            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(topicIdStr);
                topicService.deleteTopic(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/topics");
    }
}
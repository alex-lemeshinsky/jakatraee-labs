package com.example.forum.servlets;

import com.example.forum.model.Topic;
import com.example.forum.model.User;
import com.example.forum.service.TopicClosureException;
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

@WebServlet({"/topics", "/topics/edit"})
public class TopicServlet extends HttpServlet {

    @EJB
    private TopicService topicService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/topics/edit".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Long id = Long.parseLong(idParam);
                Topic topic = topicService.getTopicById(id);

                if (topic != null) {
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
                if (title != null && !title.trim().isEmpty()) {
                    Topic topic = topicService.getTopicById(id);
                    if (topic == null) {
                        throw new TopicClosureException("Тему не знайдено.");
                    }
                    topic.setTitle(title);
                    topic.setDescription(description);
                    topicService.updateTopic(topic);
                }
                session.setAttribute("flashMessage", "Тему оновлено.");
                resp.sendRedirect(req.getContextPath() + "/topics");
                return;
            } else if ("close-transactional".equals(action)) {
                Long id = Long.parseLong(topicIdStr);
                boolean simulateFailure = "true".equals(req.getParameter("simulateFailure"));
                topicService.closeTopic(id, simulateFailure);
                session.setAttribute("flashMessage",
                        simulateFailure
                                ? "Сценарій rollback відпрацював: зміни не збережені."
                                : "Тему успішно закрито, а пов'язані дописи оновлено в межах однієї транзакції.");
            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(topicIdStr);
                topicService.deleteTopic(id);
                session.setAttribute("flashMessage", "Тему видалено.");
            }
        } catch (TopicClosureException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashError", "Під час виконання операції сталася помилка.");
        }
        resp.sendRedirect(req.getContextPath() + "/topics");
    }
}

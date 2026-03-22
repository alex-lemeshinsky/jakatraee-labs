package com.example.forum.servlets;

import com.example.forum.model.Post;
import com.example.forum.model.Topic;
import com.example.forum.model.User;
import com.example.forum.service.PostService;
import com.example.forum.service.TopicService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    @EJB
    private PostService postService;

    @EJB
    private TopicService topicService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long topicId = Long.parseLong(req.getParameter("topicId"));
        Topic topic = topicService.getTopicById(topicId);
        topic.setPosts(postService.getPostsByTopicId(topicId));
        req.setAttribute("topic", topic);
        req.getRequestDispatcher("/WEB-INF/views/posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        String topicIdStr = req.getParameter("topicId");

        try {
            if ("delete".equals(action)) {
                Long postId = Long.parseLong(req.getParameter("postId"));
                Post post = postService.getPostById(postId);
                if (post != null) {
                    boolean isAdmin = "admin".equals(user.getRole());
                    boolean isAuthor = post.getAuthor().getUsername().equals(user.getUsername());

                    if (isAdmin || isAuthor) {
                        postService.deletePost(postId);
                    } else {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Ви не можете видалити чужий допис");
                        return;
                    }
                }
            } else {
                String content = req.getParameter("content");
                if (content != null && !content.trim().isEmpty()) {
                    Long topicId = Long.parseLong(topicIdStr);

                    Post post = new Post(null, content, user, topicId);
                    postService.createPost(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/posts?topicId=" + topicIdStr);
    }
}

package com.example.forum.servlets;

import com.example.forum.dao.UserDAO;
import com.example.forum.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @EJB
    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String identifier = req.getParameter("username");
        String password = req.getParameter("password");

        Optional<User> authenticatedUser = userDAO.authenticate(identifier, password);

        if (authenticatedUser.isPresent()) {
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("user", authenticatedUser.get());
            resp.sendRedirect(req.getContextPath() + "/topics");
        } else {
            req.setAttribute("error", "Невірний логін/email або пароль");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
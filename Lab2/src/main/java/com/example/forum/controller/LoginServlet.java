package com.example.forum.controller;

import com.example.forum.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = null;

        // заглушка для визначення ролі
        if ("admin".equals(username) && "admin".equals(password)) {
            role = "admin";
        } else if ("user".equals(username) && "user".equals(password)) {
            role = "user";
        }

        if (role != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", new User(username, role));
        }
        resp.sendRedirect("/topics");
    }
}

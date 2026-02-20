package com.example.forum.controller;

import com.example.forum.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        // Приклад валідації
        if (password == null || !password.equals(confirm)) {
            req.setAttribute("error", "Паролі не співпадають");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // Тут буде логіка створення користувача (заглушка)
        // Наприклад: User newUser = new User(username, "user");
        // session.setAttribute("user", newUser);
        // або збереження в сервіс/БД

        HttpSession session = req.getSession();
        session.setAttribute("user", new User(username, "user"));

        resp.sendRedirect("/topics");
    }
}

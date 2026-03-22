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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @EJB
    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        if (password == null || !password.equals(confirm)) {
            sendError(req, resp, "Паролі не співпадають");
            return;
        }

        if (password.length() < 8) {
            sendError(req, resp, "Пароль має бути не менше 8 символів");
            return;
        }

        if (userDAO.exists(username)) {
            sendError(req, resp, "Користувач з таким логіном вже існує");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("user");

        userDAO.save(newUser);

        HttpSession session = req.getSession();
        session.setAttribute("user", newUser);

        resp.sendRedirect(req.getContextPath() + "/topics");
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}

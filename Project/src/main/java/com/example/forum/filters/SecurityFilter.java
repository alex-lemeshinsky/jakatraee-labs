package com.example.forum.filters;

import com.example.forum.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();
        String method = req.getMethod();

        boolean isStatic = path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images");
        boolean isAuthAction = path.equals("/login") || path.equals("/register") || path.equals("/logout");

        if (isStatic || isAuthAction) {
            chain.doFilter(request, response);
            return;
        }

        boolean isViewable = path.isEmpty() || path.equals("/") ||
                path.equals("/home") || path.equals("/topics") ||
                path.equals("/posts");

        if ("GET".equalsIgnoreCase(method) && isViewable) {
            chain.doFilter(request, response);
            return;
        }

        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (path.equals("/topics/edit") && !"admin".equals(user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ дозволено лише адміністраторам");
            return;
        }
        String action = req.getParameter("action");
        boolean isAdminAction = "update".equals(action) || "delete".equals(action) || "create".equals(action);

        if (path.equals("/topics") && isAdminAction && !"admin".equals(user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас немає прав для цієї дії");
            return;
        }
        chain.doFilter(request, response);
    }
}
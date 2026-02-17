package com.example.app1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "memberServlet", urlPatterns = "/member")
public class MemberServlet extends HttpServlet {

    private final Map<String, Member> members = new LinkedHashMap<>();

    @Override
    public void init() {
        members.put("lemeshynskyi", new Member(
                "Лемешинський Олексій Сергійович", "ІП-52мп",
                "ФІОТ", "121 Інженерія програмного забезпечення"));
        members.put("miloserdov", new Member(
                "Мілосердов Вадим Валентинович", "ІП-52мп",
                "ФІОТ", "121 Інженерія програмного забезпечення"));
        members.put("khrushch", new Member(
                "Хрущ Андрій Володимирович", "ІП-54мп",
                "ФІОТ", "121 Інженерія програмного забезпечення"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");

        if (id == null || !members.containsKey(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
            return;
        }

        request.setAttribute("member", members.get(id));
        request.setAttribute("members", members);
        request.getRequestDispatcher("/WEB-INF/views/member.jsp").forward(request, response);
    }
}

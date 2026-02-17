package com.example.app1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "memberServlet", urlPatterns = "/member")
public class MemberServlet extends HttpServlet {

    private record Member(String fullName, String group, String faculty, String specialty) {}

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        String id = request.getParameter("id");

        if (id == null || !members.containsKey(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
            return;
        }

        Member member = members.get(id);
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <title>" + member.fullName() + "</title>");
        out.println("  <link rel=\"stylesheet\" href=\"css/style.css\">");
        out.println("</head>");
        out.println("<body>");

        out.println("  <h1>" + member.fullName() + "</h1>");

        out.println("  <nav>");
        out.println("    <a href=\"index.html\">Головна</a>");
        for (var entry : members.entrySet()) {
            String name = shortenName(entry.getValue().fullName());
            out.println("    <a href=\"member?id=" + entry.getKey() + "\">" + name + "</a>");
        }
        out.println("  </nav>");

        out.println("  <img src=\"images/logo.png\" alt=\"Logo\" class=\"logo\">");
        out.println("  <table>");
        out.println("    <tr><td>Група:</td><td>" + member.group() + "</td></tr>");
        out.println("    <tr><td>Факультет:</td><td>" + member.faculty() + "</td></tr>");
        out.println("    <tr><td>Спеціальність:</td><td>" + member.specialty() + "</td></tr>");
        out.println("    <tr><td>Університет:</td><td>");
        out.println("      <a href=\"https://kpi.ua\" target=\"_blank\">КПІ ім. Ігоря Сікорського</a>");
        out.println("    </td></tr>");
        out.println("  </table>");

        out.println("  <p><a href=\"index.html\">&larr; На головну</a></p>");

        out.println("</body>");
        out.println("</html>");
    }

    private String shortenName(String fullName) {
        String[] parts = fullName.split(" ");
        if (parts.length >= 3) {
            return parts[0] + " " + parts[1].charAt(0) + "." + parts[2].charAt(0) + ".";
        }
        return fullName;
    }
}

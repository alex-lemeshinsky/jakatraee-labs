<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.app1.Member" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>${member.fullName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>${member.fullName}</h1>

<nav>
    <a href="${pageContext.request.contextPath}/index.html">Головна</a>
    <%
        Map<String, Member> allMembers = (Map<String, Member>) request.getAttribute("members");
        if (allMembers != null) {
            for (Map.Entry<String, Member> entry : allMembers.entrySet()) {
    %>
    <a href="${pageContext.request.contextPath}/member?id=<%= entry.getKey() %>"><%= entry.getValue().getShortName() %></a>
    <%
            }
        }
    %>
</nav>

<img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="logo">
<table>
    <tr><td>Група:</td><td>${member.group}</td></tr>
    <tr><td>Факультет:</td><td>${member.faculty}</td></tr>
    <tr><td>Спеціальність:</td><td>${member.specialty}</td></tr>
    <tr><td>Університет:</td><td>
        <a href="https://kpi.ua" target="_blank">КПІ ім. Ігоря Сікорського</a>
    </td></tr>
</table>

<p><a href="${pageContext.request.contextPath}/index.html">&larr; На головну</a></p>
</body>
</html>

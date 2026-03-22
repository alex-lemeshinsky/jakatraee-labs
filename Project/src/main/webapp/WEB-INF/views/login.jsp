<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Увійти — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="auth-form">
        <h1>Увійти</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" style="color: #e74c3c; background: #fdf2f2; padding: 12px; border-radius: 6px; margin-bottom: 1.5rem; border: 1px solid #e74c3c; font-size: 0.9rem;">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Логін або email</label>
                <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">
                Увійти
            </button>
        </form>
        <p style="text-align: center; margin-top: 1.5rem; color: var(--muted);">
            Ще немає акаунта?
            <a href="${pageContext.request.contextPath}/register" style="color: var(--accent); text-decoration: none;">
                Зареєструватися
            </a>
        </p>
    </div>
</div>
<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
        <small>Навчальний проєкт · Дані зберігаються в PostgreSQL (Docker)</small>
    </div>
</footer>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Реєстрація — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="auth-form">
        <h1>Реєстрація</h1>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="username">Логін (використовуватиметься для входу)</label>
                <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required
                       placeholder="Мінімум 8 символів">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Повторіть пароль</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-user-plus"></i> Зареєструватися
            </button>
        </form>
        <p style="text-align: center; margin-top: 1.5rem; color: var(--muted);">
            Вже маєте акаунт?
            <a href="${pageContext.request.contextPath}/login" style="color: var(--accent); text-decoration: none;">
                Увійти
            </a>
        </p>
    </div>
</div>
<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
        <small>Навчальний проєкт · Дані зберігаються тільки в пам'яті</small>
    </div>
</footer>
</body>
</html>
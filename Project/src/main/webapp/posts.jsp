<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Дописи: <c:out value="${topic.title}" /> — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<header>
    <div class="container">
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/" class="nav-brand">Говоримо Відкрито</a>

            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/topics">Теми</a>
                <a href="${pageContext.request.contextPath}/topics" class="active">Дописи</a>

                <c:if test="${not empty sessionScope.user}">
                        <span class="user-info">
                            <i class="fas fa-user"></i>
                            <c:out value="${sessionScope.user.username}" escapeXml="true"/>
                            (${sessionScope.user.role})
                        </span>
                    <a href="${pageContext.request.contextPath}/logout">Вийти</a>
                </c:if>

                <c:if test="${empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/login">Увійти</a>
                    <a href="${pageContext.request.contextPath}/register">Реєстрація</a>
                </c:if>
            </div>
        </nav>
    </div>
</header>

<div class="container">
    <div style="margin: 2rem 0;">
        <a href="${pageContext.request.contextPath}/topics" class="btn btn-outline">
            <i class="fas fa-arrow-left"></i> Назад до тем
        </a>
    </div>

    <div class="card">
        <h1 style="margin-bottom: 0.5rem;"><c:out value="${topic.title}" /></h1>
        <p style="color: var(--muted); margin-bottom: 2rem;">
            <c:out value="${topic.description}" />
        </p>

        <c:if test="${empty topic.posts}">
            <p style="text-align: center; padding: 2rem; color: var(--muted);">
                У цій темі ще немає дописів. Будьте першим!
            </p>
        </c:if>

        <c:forEach var="post" items="${topic.posts}">
            <div class="post-card">
                <div class="post-author">
                    <i class="fas fa-user-circle"></i>
                    <c:out value="${post.author.username}" />
                    <small style="color: var(--muted); margin-left: 0.5rem;">
                        <c:out value="${post.createdAt}" /> <!-- якщо є дата -->
                    </small>
                </div>
                <div class="post-content">
                        ${fn:escapeXml(post.content)}
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Форма додавання нового допису (тільки для авторизованих) -->
    <c:if test="${not empty sessionScope.user && (sessionScope.user.role == 'user' || sessionScope.user.role == 'admin')}">
        <div class="card" style="margin-top: 2.5rem;">
            <h2 style="margin-bottom: 1.5rem;">Додати свій допис</h2>
            <form action="${pageContext.request.contextPath}/posts" method="post">
                <input type="hidden" name="topicId" value="${topic.id}">

                <div class="form-group">
                    <label for="content">Ваш допис</label>
                    <textarea id="content" name="content" rows="6" required
                              placeholder="Напишіть свій коментар чи думку..."></textarea>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    <i class="fas fa-paper-plane"></i> Опублікувати
                </button>
            </form>
        </div>
    </c:if>

    <c:if test="${empty sessionScope.user}">
        <div class="card" style="margin-top: 2.5rem; text-align: center; padding: 2rem;">
            <p style="margin-bottom: 1rem; color: var(--muted);">
                Щоб додати допис, потрібно увійти в акаунт
            </p>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                Увійти
            </a>
            <span style="margin: 0 1rem; color: var(--muted);">або</span>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">
                Зареєструватися
            </a>
        </div>
    </c:if>
</div>

<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
        <small>Навчальний проєкт · Дані зберігаються тільки в пам'яті</small>
    </div>
</footer>

</body>
</html>
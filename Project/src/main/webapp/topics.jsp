<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Теми форуму — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="top-bar">
    <div class="container" style="display: flex; align-items: center; padding: 1rem 0; gap: 1rem;">
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline btn-icon" style="margin-right: auto;">
            <i class="fas fa-home"></i>
            <span>На головну</span>
        </a>

        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-icon">
                <i class="fas fa-sign-in-alt"></i>
                <span>Увійти</span>
            </a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-icon">
                <i class="fas fa-user-plus"></i>
                <span>Реєстрація</span>
            </a>
        </c:if>

        <c:if test="${not empty sessionScope.user}">
            <span class="user-info">
                <i class="fas fa-user-circle"></i>
                <c:out value="${sessionScope.user.username}" escapeXml="true"/>
                <small>(${sessionScope.user.role})</small>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-icon">
                <i class="fas fa-sign-out-alt"></i>
                <span>Вийти</span>
            </a>
        </c:if>
    </div>
</div>
<div class="container" style="padding-top: 1rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin: 2rem 0 1.5rem;">
        <h1 style="margin: 0;">Теми форуму</h1>

        <c:if test="${sessionScope.user.role == 'admin'}">
            <a href="#" class="btn btn-primary">
                <i class="fas fa-plus"></i> Створити тему
            </a>
        </c:if>
    </div>

    <!-- Список тем як картки -->
    <div class="cards">
        <c:forEach var="topic" items="${topics}">
            <div class="card topic-card">
                <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                    <div style="flex: 1;">
                        <h3 style="margin: 0 0 0.6rem 0;">
                            <a href="${pageContext.request.contextPath}/posts?topicId=${topic.id}" style="color: var(--text); text-decoration: none;">
                                <c:out value="${topic.title}"/>
                            </a>
                        </h3>
                        <p style="color: var(--muted); margin-bottom: 0.8rem;">
                            <c:out value="${topic.description}"/>
                        </p>
                        <div style="display: flex; gap: 1.5rem; font-size: 0.9rem; color: var(--muted);">
                                <span>
                                    <i class="far fa-calendar-alt"></i>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${not empty topic.createdAt}">--%>
<%--                                            <fmt:formatDate value="${topic.createdAt}" pattern="dd.MM.yyyy HH:mm"/>--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>—</c:otherwise>--%>
<%--                                    </c:choose>--%>
                                </span>
                            <span>
                                    <i class="far fa-comment-dots"></i>
                                    ${topic.posts != null ? topic.posts.size() : 0} дописів
                                </span>
                        </div>
                    </div>

                    <div style="text-align: right; min-width: 120px;">
                        <c:choose>
                            <c:when test="${topic.closed}">
                                <span class="status-closed">Закрита</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-open">Відкрита</span>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${sessionScope.user.role == 'admin'}">
                            <form action="${pageContext.request.contextPath}/topics" method="post" style="margin-top: 1rem;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${topic.id}">
                                <button type="submit" class="btn btn-small" style="background: var(--error); color: white; width: 100%;">
                                    Видалити
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty topics}">
            <div class="card" style="text-align: center; padding: 3rem; color: var(--muted);">
                <h3>Поки що немає тем</h3>
                <p>Будьте першим, хто створить обговорення!</p>
            </div>
        </c:if>
    </div>

    <!-- Форма створення теми для адміна -->
    <c:if test="${sessionScope.user.role == 'admin'}">
        <div class="card" style="margin-top: 3rem;">
            <h2 style="margin-bottom: 1.5rem;">Створити нову тему</h2>
            <form action="${pageContext.request.contextPath}/topics" method="post">
                <input type="hidden" name="action" value="create">

                <div class="form-group">
                    <label for="title">Назва теми</label>
                    <input type="text" id="title" name="title" required placeholder="Введіть назву теми">
                </div>

                <div class="form-group">
                    <label for="description">Опис</label>
                    <textarea id="description" name="description" rows="4" required placeholder="Короткий опис теми..."></textarea>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    Створити тему
                </button>
            </form>
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
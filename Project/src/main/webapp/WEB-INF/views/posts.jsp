<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Дописи: <c:out value="${topic.title}" /> — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .card:hover {
            transform: none !important;
            border-color: var(--border) !important;
            box-shadow: none !important;
        }
    </style>
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
<div class="container">
    <div style="margin: 2rem 0;">
        <a href="${pageContext.request.contextPath}/topics" class="btn btn-outline">
            <i class="fas fa-arrow-left"></i> Назад до тем
        </a>
    </div>
    <div class="card topic-detail">
        <h1 style="margin-bottom: 0.5rem;"><c:out value="${topic.title}" /></h1>
        <p style="color: var(--muted); margin-bottom: 2rem; font-size: 1.1rem;">
            <c:out value="${topic.description}" />
        </p>
        <c:if test="${empty topic.posts}">
            <div style="text-align: center; padding: 3rem 1rem; color: var(--muted); background: rgba(255,255,255,0.03); border-radius: 12px;">
                <h3 style="margin-bottom: 0.8rem;">У цій темі ще немає дописів</h3>
                <p>Будьте першим, хто напише думку!</p>
            </div>
        </c:if>

        <div class="posts-list">
            <c:forEach var="post" items="${topic.posts}">
                <div class="post-item">
                    <div class="post-header">
                        <div class="post-avatar">
                            <i class="fas fa-user-circle fa-2x"></i>
                        </div>
                        <div class="post-meta">
                            <strong><c:out value="${post.author.username}" /></strong>
                            <small>
                                <fmt:formatDate value="${post.createdAt}" pattern="dd.MM.yy HH:mm" />
                            </small>
                        </div>
                    </div>
                    <div class="post-body">
                        <div class="post-content user-generated-content">${fn:escapeXml(post.content)}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <c:choose>
        <c:when test="${topic.closed}">
            <div class="card auth-prompt-card" style="border-color: var(--error); background: rgba(231, 76, 60, 0.05);">
                <i class="fas fa-lock fa-3x" style="color: var(--error); margin-bottom: 1rem; display: block;"></i>
                <h3 style="color: var(--error);">Обговорення закрито</h3>
                <p style="color: var(--muted);">Адміністратор закрив цю тему для нових дописів.</p>
            </div>
        </c:when>
        <c:when test="${empty sessionScope.user}">
            <div class="card auth-prompt-card">
                <i class="fas fa-user-lock fa-3x" style="color: var(--muted); margin-bottom: 1rem; display: block;"></i>
                <h3>Хочете долучитися?</h3>
                <p style="color: var(--muted); margin-bottom: 1.5rem;">Увійдіть, щоб залишати коментарі.</p>
                <div style="display: flex; gap: 1rem; justify-content: center;">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Увійти</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Реєстрація</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card card-static" style="margin-top: 2.5rem;">
                <h2>Додати свій допис</h2>
                <form action="${pageContext.request.contextPath}/posts" method="post">
                    <input type="hidden" name="topicId" value="${topic.id}">
                    <div class="form-group" style="margin-top: 10px">
                        <textarea name="content" rows="6" required placeholder="Ваш коментар..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%;">Опублікувати</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
        <small>Навчальний проєкт · Дані зберігаються в PostgreSQL (Docker)</small>
    </div>
</footer>
</body>
</html>
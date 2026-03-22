<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редагувати тему — Говоримо Відкрито</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* Забороняємо розтягування, як ти просив */
        textarea {
            resize: none;
        }
        /* Статична картка без анімації при наведенні */
        .card-static:hover {
            transform: none !important;
            box-shadow: none !important;
            border-color: var(--border) !important;
        }
    </style>
</head>
<body>

<div class="top-bar">
    <div class="container" style="display: flex; align-items: center; padding: 1rem 0;">
        <a href="${pageContext.request.contextPath}/topics" class="btn btn-outline btn-icon">
            <i class="fas fa-arrow-left"></i>
            <span>Назад до списку</span>
        </a>
        <h2 style="margin: 0 auto;">Панель модератора</h2>
    </div>
</div>

<div class="container" style="max-width: 800px; margin-top: 3rem;">
    <div class="card card-static">
        <h1 style="margin-bottom: 1.5rem;">
            <i class="fas fa-pencil-alt" style="color: var(--accent); margin-right: 0.5rem;"></i>
            Редагування теми
        </h1>

        <form action="${pageContext.request.contextPath}/topics" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${topic.id}">

            <div class="form-group">
                <label for="title">Назва теми</label>
                <input type="text" id="title" name="title"
                       value="<c:out value='${topic.title}'/>" required autofocus>
            </div>

            <div class="form-group">
                <label for="description">Опис теми</label>
                <textarea id="description" name="description" rows="6" required><c:out value="${topic.description}"/></textarea>
            </div>
            <div class="form-group" style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem;">
                <label for="closed" style="margin: 0; cursor: pointer;">Закрити тему для обговорення:</label>
                <input type="checkbox" id="closed" name="closed" value="true"
                ${topic.closed ? 'checked' : ''}
                       style="width: 20px; height: 20px; cursor: pointer;">
            </div>
            <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                <button type="submit" class="btn btn-primary" style="flex: 2;">
                    <i class="fas fa-save"></i> Зберегти зміни
                </button>
                <a href="${pageContext.request.contextPath}/topics" class="btn btn-outline" style="flex: 1; text-align: center;">
                    Скасувати
                </a>
            </div>
        </form>
    </div>
</div>

<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
    </div>
</footer>

</body>
</html>
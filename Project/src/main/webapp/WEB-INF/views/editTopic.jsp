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
            <div class="form-group" style="margin-bottom: 1.5rem;">
                <label style="margin-bottom: 0.4rem; display: block;">Поточний статус теми</label>
                <c:choose>
                    <c:when test="${topic.closed}">
                        <span class="status-closed">Закрита</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status-open">Відкрита</span>
                    </c:otherwise>
                </c:choose>
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

        <hr style="margin: 2rem 0; border: none; border-top: 1px solid var(--border);">

        <div>
            <h2 style="margin-bottom: 0.5rem;">Демонстрація транзакційності</h2>
            <p style="color: var(--muted); margin-bottom: 1rem;">
                Ця операція закриває тему та масово оновлює всі її дописи в одній container-managed transaction.
            </p>

            <c:choose>
                <c:when test="${topic.closed}">
                    <div class="card card-static" style="background: #fff8e1; border-left: 4px solid #f9a825;">
                        Тема вже закрита. Для демонстрації rollback оберіть іншу відкриту тему.
                    </div>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/topics" method="post">
                        <input type="hidden" name="action" value="close-transactional">
                        <input type="hidden" name="id" value="${topic.id}">

                        <div class="form-group" style="display: flex; align-items: center; gap: 0.8rem;">
                            <input type="checkbox" id="simulateFailure" name="simulateFailure" value="true"
                                   style="width: 20px; height: 20px; cursor: pointer;">
                            <label for="simulateFailure" style="margin: 0; cursor: pointer;">
                                Зімітувати помилку та відкат транзакції
                            </label>
                        </div>

                        <button type="submit" class="btn btn-outline" style="width: 100%; margin-top: 1rem;"
                                onclick="return confirm('Виконати транзакційне закриття теми?')">
                            <i class="fas fa-shield-alt"></i> Закрити тему транзакційно
                        </button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<footer>
    <div class="container">
        <p>© 2026 Форум «Говоримо Відкрито»</p>
    </div>
</footer>

</body>
</html>

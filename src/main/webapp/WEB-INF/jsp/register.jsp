<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация — МиниИгры</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/casino.css">
</head>
<body>

<div class="auth-wrap">
    <div class="auth-card">
        <div class="auth-logo">
            <div class="logo-text">🎰 МиниИгры</div>
            <div class="logo-sub">Создать аккаунт</div>
        </div>

        <div class="auth-title">Регистрация</div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <span>⚠</span> ${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-group">
                <label class="form-label">Логин</label>
                <input class="form-input" name="username" placeholder="Не менее 3 символов" required autocomplete="username">
            </div>
            <div class="form-group">
                <label class="form-label">Пароль</label>
                <input class="form-input" name="password" type="password" placeholder="Не менее 6 символов" required autocomplete="new-password">
            </div>
            <button class="btn btn-gold" style="width:100%;margin-top:8px;" type="submit">
                Создать аккаунт
            </button>
        </form>

        <div class="auth-footer">
            Уже есть аккаунт?
            <a href="${pageContext.request.contextPath}/login">Войти</a>
        </div>
    </div>
</div>

</body>
</html>

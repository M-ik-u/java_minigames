<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>🎰 МиниИгры</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/casino.css">
</head>
<body>

<header class="site-header">
    <a href="${pageContext.request.contextPath}/home" class="site-logo">
        <svg viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg" aria-label="logo">
            <rect x="2" y="6" width="24" height="16" rx="3" fill="#f0c040" opacity=".15"/>
            <rect x="2" y="6" width="24" height="16" rx="3" stroke="#f0c040" stroke-width="1.5"/>
            <circle cx="9" cy="14" r="3" fill="#f0c040" opacity=".7"/>
            <circle cx="19" cy="14" r="3" fill="#f0c040" opacity=".7"/>
            <rect x="12.5" y="11" width="3" height="6" rx="1.5" fill="#f0c040"/>
        </svg>
        МиниИгры
    </a>

    <c:if test="${not empty sessionScope.user}">
        <a href="${pageContext.request.contextPath}/game/slot"     class="nav-link">🎰 Слоты</a>
        <a href="${pageContext.request.contextPath}/game/roulette" class="nav-link">🎡 Рулетка</a>
        <a href="${pageContext.request.contextPath}/game/blackjack" class="nav-link">🃏 Блэкджек</a>
        <a href="${pageContext.request.contextPath}/cashier"       class="nav-link">💳 Касса</a>
        <a href="${pageContext.request.contextPath}/history"       class="nav-link">📊 История</a>
        <c:if test="${sessionScope.user.admin}">
            <a href="${pageContext.request.contextPath}/admin/users" class="nav-link nav-admin-btn">⚙ Админ</a>
        </c:if>

        <span class="nav-spacer"></span>

        <div class="nav-balance">
            <span class="uname">${sessionScope.user.username}</span>
            <span class="amt">${sessionScope.user.balance}</span>
            <span style="font-size:.75rem;color:var(--text-faint)">кр.</span>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">Выйти</a>
    </c:if>
</header>

<main>

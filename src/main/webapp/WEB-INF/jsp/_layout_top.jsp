<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мини-игры</title>
    <style>
        body { font-family: system-ui, sans-serif; margin: 0; background: #1b1d22; color: #eee; }
        header { background: #2a2d34; padding: 12px 20px; display: flex; gap: 16px; align-items: center; }
        header a { color: #ffd54a; text-decoration: none; margin-right: 8px; }
        header .balance { margin-left: auto; color: #8bf18b; }
        main { max-width: 900px; margin: 24px auto; padding: 0 16px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border-bottom: 1px solid #333; padding: 6px 8px; text-align: left; }
        input, select, button { padding: 6px 10px; font-size: 14px; }
        button { background: #ffd54a; border: none; color: #111; cursor: pointer; }
        .err { color: #ff7878; }
        .ok { color: #8bf18b; }
        .card { background: #2a2d34; padding: 16px; border-radius: 6px; margin-bottom: 16px; }
        .reel { font-size: 64px; display: inline-block; padding: 8px 18px; background: #111; border-radius: 8px; margin: 4px; }
    </style>
</head>
<body>
<header>
    <a href="${pageContext.request.contextPath}/home">🎰 Мини-игры</a>
    <c:if test="${not empty sessionScope.user}">
        <a href="${pageContext.request.contextPath}/game/slot">Слоты</a>
        <a href="${pageContext.request.contextPath}/game/roulette">Рулетка</a>
        <a href="${pageContext.request.contextPath}/game/blackjack">Блэкджек</a>
        <a href="${pageContext.request.contextPath}/cashier">Касса</a>
        <a href="${pageContext.request.contextPath}/history">История</a>
        <c:if test="${sessionScope.user.admin}">
            <a href="${pageContext.request.contextPath}/admin/users">Админ</a>
        </c:if>
        <span class="balance">${sessionScope.user.username}: <b>${sessionScope.user.balance}</b> кр.</span>
        <a href="${pageContext.request.contextPath}/logout">Выйти</a>
    </c:if>
</header>
<main>

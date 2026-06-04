<%@ include file="_layout_top.jsp" %>

<div class="welcome-banner">
    <h2>Добро пожаловать, ${sessionScope.user.username}!</h2>
    <p>Баланс: <b style="color:var(--green)">${sessionScope.user.balance}</b> кредитов &nbsp;·&nbsp; Выберите игру и испытайте удачу</p>
</div>

<div class="card">
    <div class="card-title">🎮 Игры</div>
    <div class="card-subtitle">Все игры используют честный генератор случайных чисел SecureRandom</div>

    <div class="games-grid">
        <a href="${pageContext.request.contextPath}/game/slot" class="game-card">
            <span class="game-icon">🎰</span>
            <div class="game-name">Слоты</div>
            <div class="game-desc">3 барабана · 5 символов<br>Джекпот ×50</div>
        </a>
        <a href="${pageContext.request.contextPath}/game/roulette" class="game-card">
            <span class="game-icon">🎡</span>
            <div class="game-name">Рулетка</div>
            <div class="game-desc">Европейская · 0–36<br>Число ×36 · Цвет ×2</div>
        </a>
        <a href="${pageContext.request.contextPath}/game/blackjack" class="game-card">
            <span class="game-icon">🃏</span>
            <div class="game-name">Блэкджек</div>
            <div class="game-desc">Hit / Stand / Double<br>Блэкджек 3:2</div>
        </a>
    </div>
</div>

<%@ include file="_layout_bottom.jsp" %>

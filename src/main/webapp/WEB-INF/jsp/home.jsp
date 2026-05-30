<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Добро пожаловать, ${sessionScope.user.username}!</h2>
    <p>Баланс: <b>${sessionScope.user.balance}</b> кредитов.</p>
    <p>Выберите игру в верхнем меню или пополните счёт в кассе.</p>
</div>
<%@ include file="_layout_bottom.jsp" %>

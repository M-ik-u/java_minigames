<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Касса</h2>
    <p>Текущий баланс: <b>${sessionScope.user.balance}</b> кр.</p>
    <c:if test="${not empty ok}"><p class="ok">${ok}</p></c:if>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>

    <form method="post" action="${pageContext.request.contextPath}/cashier" style="display:inline-block; margin-right:30px;">
        <input type="hidden" name="action" value="deposit">
        <input name="amount" type="number" step="0.01" min="0.01" placeholder="Сумма" required>
        <button type="submit">Пополнить</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/cashier" style="display:inline-block;">
        <input type="hidden" name="action" value="withdraw">
        <input name="amount" type="number" step="0.01" min="0.01" placeholder="Сумма" required>
        <button type="submit">Вывести</button>
    </form>
</div>
<%@ include file="_layout_bottom.jsp" %>

<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>История транзакций</h2>
    <table>
        <tr><th>Дата</th><th>Тип</th><th>Сумма</th><th>Остаток</th></tr>
        <c:forEach var="t" items="${txs}">
            <tr>
                <td>${t.createdAt}</td>
                <td>${t.type}</td>
                <td>${t.amount}</td>
                <td>${t.balanceAfter}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="card">
    <h2>История игр</h2>
    <table>
        <tr><th>Дата</th><th>Игра</th><th>Ставка</th><th>Выплата</th><th>Результат</th></tr>
        <c:forEach var="g" items="${games}">
            <tr>
                <td>${g.createdAt}</td>
                <td>${g.game}</td>
                <td>${g.bet}</td>
                <td>${g.payout}</td>
                <td>${g.result}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="_layout_bottom.jsp" %>

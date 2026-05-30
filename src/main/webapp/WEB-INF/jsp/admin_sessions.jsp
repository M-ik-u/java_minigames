<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Игровые сессии (последние 200)</h2>
    <p>
        <a href="${pageContext.request.contextPath}/admin/users">Пользователи</a> |
        <a href="${pageContext.request.contextPath}/admin/stats">Статистика</a>
    </p>
    <table>
        <tr><th>Дата</th><th>User ID</th><th>Игра</th><th>Ставка</th><th>Выплата</th><th>Результат</th></tr>
        <c:forEach var="s" items="${sessions}">
            <tr>
                <td>${s.createdAt}</td>
                <td>${s.userId}</td>
                <td>${s.game}</td>
                <td>${s.bet}</td>
                <td>${s.payout}</td>
                <td>${s.result}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="_layout_bottom.jsp" %>

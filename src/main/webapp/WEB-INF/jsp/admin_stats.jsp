<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Статистика</h2>
    <p>
        <a href="${pageContext.request.contextPath}/admin/users">Пользователи</a> |
        <a href="${pageContext.request.contextPath}/admin/sessions">Игровые сессии</a>
    </p>
    <p>Всего пользователей: <b>${userCount}</b></p>
    <p>Общий оборот кредитов: <b>${turnover}</b></p>
</div>
<%@ include file="_layout_bottom.jsp" %>

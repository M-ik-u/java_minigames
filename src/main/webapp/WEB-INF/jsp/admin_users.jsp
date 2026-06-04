<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Пользователи</h2>
    <p>
        <a href="${pageContext.request.contextPath}/admin/users">Все</a> |
        <a href="${pageContext.request.contextPath}/admin/stats">Статистика</a> |
        <a href="${pageContext.request.contextPath}/admin/sessions">Игровые сессии</a>
    </p>
    <form method="get" action="${pageContext.request.contextPath}/admin/users">
        <input name="q" value="${q}" placeholder="Поиск по логину">
        <button type="submit">Найти</button>
    </form>
    <table>
        <tr><th>ID</th><th>Логин</th><th>Роль</th><th>Баланс</th><th>Активен</th><th>Создан</th><th></th></tr>
        <c:forEach var="u" items="${users}">
            <tr>
                <td>${u.id}</td><td>${u.username}</td><td>${u.role}</td>
                <td>${u.balance}</td><td>${u.active}</td><td>${u.createdAt}</td>
                <td>
                    <%--
                        FIX: убран скрытый input "active" — EL не поддерживает !boolean в value,
                        поэтому туда всегда попадала строка "false" или пустота.
                        Теперь AdminServlet сам читает текущий статус из БД и инвертирует его.
                    --%>
                    <form method="post" action="${pageContext.request.contextPath}/admin/toggle">
                        <input type="hidden" name="id" value="${u.id}">
                        <button type="submit">${u.active ? 'Заблокировать' : 'Разблокировать'}</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="_layout_bottom.jsp" %>

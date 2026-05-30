<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Вход</h2>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <p><input name="username" placeholder="Логин" required></p>
        <p><input name="password" type="password" placeholder="Пароль" required></p>
        <button type="submit">Войти</button>
        <a href="${pageContext.request.contextPath}/register">Регистрация</a>
    </form>
</div>
<%@ include file="_layout_bottom.jsp" %>

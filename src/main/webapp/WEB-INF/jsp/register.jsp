<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>Регистрация</h2>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <p><input name="username" placeholder="Логин (не менее 3 символов)" required></p>
        <p><input name="password" type="password" placeholder="Пароль (не менее 6 символов)" required></p>
        <button type="submit">Создать аккаунт</button>
        <a href="${pageContext.request.contextPath}/login">У меня уже есть аккаунт</a>
    </form>
</div>
<%@ include file="_layout_bottom.jsp" %>

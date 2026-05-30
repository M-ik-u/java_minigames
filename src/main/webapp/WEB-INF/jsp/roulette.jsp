<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>🎡 Европейская рулетка</h2>
    <p>Ставки: число (×36), цвет / чёт-нечёт (×2). Можно делать несколько ставок одновременно.</p>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>

    <c:if test="${not empty outcome}">
        <p style="font-size:24px;">
            Выпало: <b>${outcome.number}</b>
            <c:choose>
                <c:when test="${outcome.number == 0}">(ZERO)</c:when>
                <c:when test="${isRed}">(🔴 RED)</c:when>
                <c:otherwise>(⚫ BLACK)</c:otherwise>
            </c:choose>
        </p>
        <p>Сумма ставок: ${outcome.totalBet}, выплата:
            <c:choose>
                <c:when test="${outcome.totalPayout > 0}"><span class="ok">${outcome.totalPayout}</span></c:when>
                <c:otherwise>0</c:otherwise>
            </c:choose>
        </p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/game/roulette">
        <table>
            <tr><td>🔴 Красное</td><td><input name="amount_red" type="number" step="0.01" min="0"></td></tr>
            <tr><td>⚫ Чёрное</td><td><input name="amount_black" type="number" step="0.01" min="0"></td></tr>
            <tr><td>Чёт</td><td><input name="amount_even" type="number" step="0.01" min="0"></td></tr>
            <tr><td>Нечёт</td><td><input name="amount_odd" type="number" step="0.01" min="0"></td></tr>
            <tr>
                <td>Число
                    <input name="number" type="number" min="0" max="36" style="width:60px;">
                </td>
                <td><input name="amount_number" type="number" step="0.01" min="0"></td>
            </tr>
        </table>
        <button type="submit">Крутить</button>
    </form>
</div>
<%@ include file="_layout_bottom.jsp" %>

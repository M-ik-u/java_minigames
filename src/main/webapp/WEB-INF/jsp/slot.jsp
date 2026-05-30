<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>🎰 Слот-машина</h2>
    <p>Три одинаковых символа в ряд: 7×50, ⭐×20, 🔔×10, 🍋×5, 🍒×3.</p>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>

    <c:if test="${not empty reels}">
        <div>
            <c:forEach var="s" items="${reels}">
                <span class="reel">
                    <c:choose>
                        <c:when test="${s == 'CHERRY'}">🍒</c:when>
                        <c:when test="${s == 'LEMON'}">🍋</c:when>
                        <c:when test="${s == 'BELL'}">🔔</c:when>
                        <c:when test="${s == 'STAR'}">⭐</c:when>
                        <c:when test="${s == 'SEVEN'}">7️⃣</c:when>
                    </c:choose>
                </span>
            </c:forEach>
        </div>
        <c:choose>
            <c:when test="${payout > 0}"><p class="ok">Выигрыш: ${payout} кр.</p></c:when>
            <c:otherwise><p>Попробуйте ещё раз!</p></c:otherwise>
        </c:choose>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/game/slot">
        <input name="bet" type="number" step="0.01" min="0.01" value="10" required>
        <button type="submit">Spin</button>
    </form>
</div>
<%@ include file="_layout_bottom.jsp" %>

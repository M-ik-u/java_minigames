<%@ include file="_layout_top.jsp" %>

<div class="card">
    <div class="card-title">🎰 Слот-машина</div>
    <div class="card-subtitle">Три одинаковых символа в ряд — и джекпот твой!</div>

    <div class="paytable">
        <div class="paytable-item"><span class="sym">7️⃣</span><span class="mult">×50</span></div>
        <div class="paytable-item"><span class="sym">⭐</span><span class="mult">×20</span></div>
        <div class="paytable-item"><span class="sym">🔔</span><span class="mult">×10</span></div>
        <div class="paytable-item"><span class="sym">🍋</span><span class="mult">×5</span></div>
        <div class="paytable-item"><span class="sym">🍒</span><span class="mult">×3</span></div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><span>⚠</span> ${error}</div>
    </c:if>

    <c:if test="${not empty reels}">
        <div class="slots-display">
            <c:forEach var="s" items="${reels}">
                <div class="reel ${payout > 0 ? 'reel-win spin' : 'spin'}">
                    <c:choose>
                        <c:when test="${s == 'CHERRY'}">🍒</c:when>
                        <c:when test="${s == 'LEMON'}">🍋</c:when>
                        <c:when test="${s == 'BELL'}">🔔</c:when>
                        <c:when test="${s == 'STAR'}">⭐</c:when>
                        <c:when test="${s == 'SEVEN'}">7️⃣</c:when>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
        <c:choose>
            <c:when test="${payout > 0}">
                <div class="win-banner">🏆 Выигрыш: ${payout} кредитов!</div>
            </c:when>
            <c:otherwise>
                <div class="lose-banner">Не повезло — попробуйте ещё раз!</div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${empty reels}">
        <div class="slots-display">
            <div class="reel">❓</div>
            <div class="reel">❓</div>
            <div class="reel">❓</div>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/game/slot">
        <div class="bet-row">
            <input class="form-input" name="bet" type="number" step="0.01" min="0.01" value="${not empty lastBet ? lastBet : 10}" required placeholder="Ставка">
            <button class="btn btn-gold" type="submit">🎰 Spin!</button>
        </div>
    </form>
</div>

<%@ include file="_layout_bottom.jsp" %>

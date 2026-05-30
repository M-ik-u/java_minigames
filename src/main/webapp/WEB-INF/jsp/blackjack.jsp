<%@ include file="_layout_top.jsp" %>
<div class="card">
    <h2>🃏 Блэкджек</h2>
    <p>Дилер берёт до 17. Блэкджек 3:2. Доступно: Hit / Stand / Double.</p>
    <c:if test="${not empty error}"><p class="err">${error}</p></c:if>

    <c:choose>
        <c:when test="${empty round or round.finished}">
            <c:if test="${not empty round}">
                <p>Игрок (${round.playerScore}):
                    <c:forEach var="c" items="${round.player}"><span class="reel" style="font-size:24px;">${c}</span></c:forEach>
                </p>
                <p>Дилер (${round.dealerScore}):
                    <c:forEach var="c" items="${round.dealer}"><span class="reel" style="font-size:24px;">${c}</span></c:forEach>
                </p>
                <p>
                    <c:choose>
                        <c:when test="${round.outcome == 'BJ'}"><span class="ok">Блэкджек! Выплата: ${round.payout}</span></c:when>
                        <c:when test="${round.outcome == 'WIN'}"><span class="ok">Победа! Выплата: ${round.payout}</span></c:when>
                        <c:when test="${round.outcome == 'PUSH'}">Ничья. Возврат: ${round.payout}</c:when>
                        <c:otherwise><span class="err">Проигрыш.</span></c:otherwise>
                    </c:choose>
                </p>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/game/blackjack">
                <input type="hidden" name="action" value="deal">
                <input name="bet" type="number" step="0.01" min="0.01" value="10" required>
                <button type="submit">Раздать</button>
            </form>
        </c:when>
        <c:otherwise>
            <p>Игрок (${round.playerScore}):
                <c:forEach var="c" items="${round.player}"><span class="reel" style="font-size:24px;">${c}</span></c:forEach>
            </p>
            <p>Дилер: <span class="reel" style="font-size:24px;">${round.dealer[0]}</span>
                <span class="reel" style="font-size:24px;">?</span></p>
            <p>Ставка: <b>${round.bet}</b></p>
            <form method="post" action="${pageContext.request.contextPath}/game/blackjack" style="display:inline;">
                <input type="hidden" name="action" value="hit">
                <button type="submit">Hit</button>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/game/blackjack" style="display:inline;">
                <input type="hidden" name="action" value="stand">
                <button type="submit">Stand</button>
            </form>
            <c:if test="${round.player.size() == 2}">
                <form method="post" action="${pageContext.request.contextPath}/game/blackjack" style="display:inline;">
                    <input type="hidden" name="action" value="double">
                    <button type="submit">Double</button>
                </form>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
<%@ include file="_layout_bottom.jsp" %>

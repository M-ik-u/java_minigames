<%@ include file="_layout_top.jsp" %>

<div class="card">
    <div class="card-title">🃏 Блэкджек</div>
    <div class="card-subtitle">Дилер берёт до 17 &nbsp;·&nbsp; Блэкджек 3:2 &nbsp;·&nbsp; Hit / Stand / Double</div>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><span>⚠</span> ${error}</div>
    </c:if>

    <c:choose>
        <%-- ── Партия завершена (показываем результат + форму новой раздачи) ── --%>
        <c:when test="${empty round or round.finished}">

            <c:if test="${not empty round}">
                <div class="bj-table">
                    <div class="bj-hand-label">Дилер (${round.dealerScore})</div>
                    <div class="bj-cards">
                        <c:forEach var="c" items="${round.dealer}">
                            <div class="card-chip">${c}</div>
                        </c:forEach>
                    </div>
                    <hr class="bj-divider">
                    <div class="bj-hand-label">Игрок (${round.playerScore})</div>
                    <div class="bj-cards">
                        <c:forEach var="c" items="${round.player}">
                            <div class="card-chip">${c}</div>
                        </c:forEach>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${round.outcome == 'BJ'}">
                        <div class="bj-outcome bj">🎉 Блэкджек! Выплата: <b>${round.payout}</b> кр.</div>
                    </c:when>
                    <c:when test="${round.outcome == 'WIN'}">
                        <div class="bj-outcome win">✅ Победа! Выплата: <b>${round.payout}</b> кр.</div>
                    </c:when>
                    <c:when test="${round.outcome == 'PUSH'}">
                        <div class="bj-outcome push">🤝 Ничья. Возврат: <b>${round.payout}</b> кр.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="bj-outcome lose">💸 Проигрыш.</div>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/game/blackjack">
                <input type="hidden" name="action" value="deal">
                <div class="bet-row">
                    <%-- FIX: берём последнюю ставку из сессии; если нет — 10 --%>
                    <input class="form-input" name="bet" type="number" step="0.01" min="0.01"
                           value="${not empty sessionScope.bjLastBet ? sessionScope.bjLastBet : 10}"
                           required placeholder="Ставка">
                    <button class="btn btn-gold" type="submit">🃏 Раздать карты</button>
                </div>
            </form>
        </c:when>

        <%-- ── Партия в процессе ── --%>
        <c:otherwise>
            <div class="bj-table">
                <div class="bj-hand-label">Дилер</div>
                <div class="bj-cards">
                    <div class="card-chip">${round.dealer[0]}</div>
                    <div class="card-chip hidden"></div>
                </div>
                <hr class="bj-divider">
                <div class="bj-hand-label">Игрок (${round.playerScore})</div>
                <div class="bj-cards">
                    <c:forEach var="c" items="${round.player}">
                        <div class="card-chip">${c}</div>
                    </c:forEach>
                </div>
            </div>

            <div style="margin-bottom:16px;color:var(--text-muted);font-size:.875rem;">
                Ставка: <b style="color:var(--gold)">${round.bet}</b> кр.
            </div>

            <div class="bj-actions">
                <form method="post" action="${pageContext.request.contextPath}/game/blackjack">
                    <input type="hidden" name="action" value="hit">
                    <button class="btn btn-gold" type="submit">Hit +</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/game/blackjack">
                    <input type="hidden" name="action" value="stand">
                    <button class="btn btn-ghost" type="submit">Stand ✋</button>
                </form>
                <c:if test="${round.player.size() == 2}">
                    <form method="post" action="${pageContext.request.contextPath}/game/blackjack">
                        <input type="hidden" name="action" value="double">
                        <button class="btn btn-ghost" type="submit">Double ×2</button>
                    </form>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="_layout_bottom.jsp" %>

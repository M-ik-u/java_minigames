<%@ include file="_layout_top.jsp" %>

<div class="card">
    <div class="card-title">🎡 Европейская рулетка</div>
    <div class="card-subtitle">Число (×36) &nbsp;·&nbsp; Красное / Чёрное (×2) &nbsp;·&nbsp; Чёт / Нечёт (×2) &nbsp;·&nbsp; Можно делать несколько ставок одновременно</div>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><span>⚠</span> ${error}</div>
    </c:if>

    <c:if test="${not empty outcome}">
        <div class="roulette-result">
            <div class="roulette-number ${outcome.number == 0 ? 'zero' : (isRed ? 'red' : 'black')}">
                ${outcome.number}
            </div>
            <div class="roulette-label">
                <c:choose>
                    <c:when test="${outcome.number == 0}">🟢 ZERO</c:when>
                    <c:when test="${isRed}">🔴 Красное</c:when>
                    <c:otherwise>⚫ Чёрное</c:otherwise>
                </c:choose>
                &nbsp;·&nbsp;
                Ставки: <b>${outcome.totalBet}</b> кр.
                &nbsp;·&nbsp;
                <c:choose>
                    <c:when test="${outcome.totalPayout > 0}">
                        Выплата: <b style="color:var(--green)">${outcome.totalPayout}</b> кр.
                    </c:when>
                    <c:otherwise>
                        <span style="color:var(--red)">Проигрыш</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>

    <%--
        FIX: поля ставок теперь берут значения из sessionScope.roulLastBets.
        При первом открытии страницы roulLastBets == null → все поля пустые (placeholder).
        После спина сервлет сохраняет введённые суммы в сессию и JSP их восстанавливает.
        При переходе на другую страницу и возврате — doGet очищает roulLastBets → поля снова пустые.
    --%>
    <form method="post" action="${pageContext.request.contextPath}/game/roulette">
        <div class="bet-grid">
            <div class="bet-field">
                <span class="bet-icon">🔴</span>
                <span class="bet-label">Красное ×2</span>
                <input class="form-input" name="amount_red" type="number" step="0.01" min="0"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.red : ''}"
                       placeholder="0">
            </div>
            <div class="bet-field">
                <span class="bet-icon">⚫</span>
                <span class="bet-label">Чёрное ×2</span>
                <input class="form-input" name="amount_black" type="number" step="0.01" min="0"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.black : ''}"
                       placeholder="0">
            </div>
            <div class="bet-field">
                <span class="bet-icon">2️⃣</span>
                <span class="bet-label">Чётное ×2</span>
                <input class="form-input" name="amount_even" type="number" step="0.01" min="0"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.even : ''}"
                       placeholder="0">
            </div>
            <div class="bet-field">
                <span class="bet-icon">1️⃣</span>
                <span class="bet-label">Нечётное ×2</span>
                <input class="form-input" name="amount_odd" type="number" step="0.01" min="0"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.odd : ''}"
                       placeholder="0">
            </div>
            <div class="bet-number-row">
                <span class="bet-icon">🎯</span>
                <span class="bet-label">Число (0–36) ×36</span>
                <input class="form-input" name="number" type="number" min="0" max="36"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.number : ''}"
                       placeholder="0–36">
                <input class="form-input" name="amount_number" type="number" step="0.01" min="0"
                       value="${not empty sessionScope.roulLastBets ? sessionScope.roulLastBets.amountNumber : ''}"
                       placeholder="Ставка">
            </div>
        </div>
        <button class="btn btn-gold" type="submit">🎡 Крутить!</button>
    </form>
</div>

<%@ include file="_layout_bottom.jsp" %>

<%@ include file="_layout_top.jsp" %>

<div class="card">
    <div class="card-title">📊 История транзакций</div>
    <div class="card-subtitle">Все финансовые операции по вашему аккаунту</div>

    <table class="data-table">
        <thead>
            <tr>
                <th>Дата</th>
                <th>Тип</th>
                <th>Сумма</th>
                <th>Остаток</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="t" items="${txs}">
                <tr>
                    <td>${t.createdAt}</td>
                    <td>
                        <c:choose>
                            <c:when test="${t.type == 'WIN'}">
                                <span class="tag tag-win">WIN</span>
                            </c:when>
                            <c:when test="${t.type == 'BET'}">
                                <span class="tag tag-bet">BET</span>
                            </c:when>
                            <c:when test="${t.type == 'DEPOSIT'}">
                                <span class="tag tag-dep">DEP</span>
                            </c:when>
                            <c:otherwise>
                                <span class="tag tag-with">${t.type}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td style="color:var(--text)">${t.amount}</td>
                    <td style="color:var(--text-muted)">${t.balanceAfter}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty txs}">
                <tr><td colspan="4" style="text-align:center;color:var(--text-faint);padding:24px">Транзакций пока нет</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<div class="card">
    <div class="card-title">🎮 История игр</div>
    <div class="card-subtitle">Все сыгранные раунды</div>

    <table class="data-table">
        <thead>
            <tr>
                <th>Дата</th>
                <th>Игра</th>
                <th>Ставка</th>
                <th>Выплата</th>
                <th>Результат</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="g" items="${games}">
                <tr>
                    <td>${g.createdAt}</td>
                    <td style="color:var(--text)">${g.game}</td>
                    <td>${g.bet}</td>
                    <td style="color:${g.payout > 0 ? 'var(--green)' : 'var(--text-faint)'}">${g.payout}</td>
                    <td style="font-size:.8rem">${g.result}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty games}">
                <tr><td colspan="5" style="text-align:center;color:var(--text-faint);padding:24px">Игр пока нет</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<%@ include file="_layout_bottom.jsp" %>

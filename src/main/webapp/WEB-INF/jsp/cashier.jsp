<%@ include file="_layout_top.jsp" %>

<div class="card">
    <div class="card-title">💳 Касса</div>
    <div class="card-subtitle">Пополните или выведите виртуальные кредиты</div>

    <div class="balance-display">
        <div class="bal-label">Текущий баланс</div>
        <div>
            <span class="bal-amount">${sessionScope.user.balance}</span>
            <span class="bal-unit">кр.</span>
        </div>
    </div>

    <c:if test="${not empty ok}">
        <div class="alert alert-success"><span>✓</span> ${ok}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><span>⚠</span> ${error}</div>
    </c:if>

    <div class="cashier-grid">
        <div class="cashier-panel">
            <div class="cashier-panel-title deposit">▲ Пополнить</div>
            <form method="post" action="${pageContext.request.contextPath}/cashier">
                <input type="hidden" name="action" value="deposit">
                <div class="form-group">
                    <label class="form-label">Сумма (кр.)</label>
                    <input class="form-input" name="amount" type="number" step="0.01" min="0.01" placeholder="0.00" required>
                </div>
                <button class="btn btn-gold btn-sm" type="submit" style="width:100%">Пополнить</button>
            </form>
        </div>

        <div class="cashier-panel">
            <div class="cashier-panel-title withdraw">▼ Вывести</div>
            <form method="post" action="${pageContext.request.contextPath}/cashier">
                <input type="hidden" name="action" value="withdraw">
                <div class="form-group">
                    <label class="form-label">Сумма (кр.)</label>
                    <input class="form-input" name="amount" type="number" step="0.01" min="0.01" placeholder="0.00" required>
                </div>
                <button class="btn btn-danger btn-sm" type="submit" style="width:100%">Вывести</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="_layout_bottom.jsp" %>

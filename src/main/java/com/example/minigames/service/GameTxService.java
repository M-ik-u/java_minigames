package com.example.minigames.service;

import com.example.minigames.dao.GameSessionDao;
import com.example.minigames.dao.TransactionDao;
import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/** Атомарно списывает ставку, начисляет выигрыш и сохраняет сессию игры. */
public class GameTxService {
    private final UserDao users = new UserDao();
    private final TransactionDao transactions = new TransactionDao();
    private final GameSessionDao sessions = new GameSessionDao();

    public BigDecimal playSingleBet(long userId, String game, BigDecimal bet, BigDecimal payout, String result) throws SQLException {
        if (bet.signum() <= 0) throw new IllegalArgumentException("Ставка должна быть положительной");
        try (Connection c = Db.conn()) {
            c.setAutoCommit(false);
            try {
                User u = users.lockById(c, userId).orElseThrow();
                if (u.getBalance().compareTo(bet) < 0) {
                    throw new IllegalArgumentException("Недостаточно средств на счёте");
                }
                BigDecimal after = u.getBalance().subtract(bet);
                users.updateBalance(c, userId, after);
                transactions.insert(c, userId, "BET", bet, after);
                if (payout.signum() > 0) {
                    after = after.add(payout);
                    users.updateBalance(c, userId, after);
                    transactions.insert(c, userId, "WIN", payout, after);
                }
                sessions.insert(c, userId, game, bet, payout, result);
                c.commit();
                return after;
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    /** Многоставочный раунд (рулетка): сумма ставок списывается, выплата начисляется единой записью. */
    public BigDecimal playMultiBet(long userId, String game, BigDecimal totalBet, BigDecimal totalPayout, String result) throws SQLException {
        return playSingleBet(userId, game, totalBet, totalPayout, result);
    }
}

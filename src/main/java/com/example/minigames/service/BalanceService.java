package com.example.minigames.service;

import com.example.minigames.dao.TransactionDao;
import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class BalanceService {
    private final UserDao users = new UserDao();
    private final TransactionDao transactions = new TransactionDao();

    public BigDecimal deposit(long userId, BigDecimal amount) throws SQLException {
        require(amount.signum() > 0, "Сумма пополнения должна быть положительной");
        return change(userId, amount, "DEPOSIT");
    }

    public BigDecimal withdraw(long userId, BigDecimal amount) throws SQLException {
        require(amount.signum() > 0, "Сумма вывода должна быть положительной");
        try (Connection c = Db.conn()) {
            c.setAutoCommit(false);
            try {
                User u = users.lockById(c, userId).orElseThrow();
                if (u.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Недостаточно средств");
                }
                BigDecimal nb = u.getBalance().subtract(amount);
                users.updateBalance(c, userId, nb);
                transactions.insert(c, userId, "WITHDRAWAL", amount, nb);
                c.commit();
                return nb;
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private BigDecimal change(long userId, BigDecimal amount, String type) throws SQLException {
        try (Connection c = Db.conn()) {
            c.setAutoCommit(false);
            try {
                User u = users.lockById(c, userId).orElseThrow();
                BigDecimal nb = u.getBalance().add(amount);
                users.updateBalance(c, userId, nb);
                transactions.insert(c, userId, type, amount, nb);
                c.commit();
                return nb;
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private static void require(boolean cond, String msg) {
        if (!cond) throw new IllegalArgumentException(msg);
    }
}

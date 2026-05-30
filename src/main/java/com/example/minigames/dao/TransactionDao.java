package com.example.minigames.dao;

import com.example.minigames.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public void insert(Connection c, long userId, String type, BigDecimal amount, BigDecimal balanceAfter) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO transactions (user_id, type, amount, balance_after) VALUES (?,?,?,?)")) {
            ps.setLong(1, userId);
            ps.setString(2, type);
            ps.setBigDecimal(3, amount);
            ps.setBigDecimal(4, balanceAfter);
            ps.executeUpdate();
        }
    }

    public List<Transaction> findByUser(Connection c, long userId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id, user_id, type, amount, balance_after, created_at " +
                        "FROM transactions WHERE user_id = ? ORDER BY created_at DESC")) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Transaction> list = new ArrayList<>();
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setId(rs.getLong("id"));
                    t.setUserId(rs.getLong("user_id"));
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getBigDecimal("amount"));
                    t.setBalanceAfter(rs.getBigDecimal("balance_after"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) t.setCreatedAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
                    list.add(t);
                }
                return list;
            }
        }
    }

    public BigDecimal totalTurnover(Connection c) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT COALESCE(SUM(amount), 0) FROM transactions");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getBigDecimal(1);
        }
    }
}

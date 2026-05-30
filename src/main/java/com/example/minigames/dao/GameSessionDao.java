package com.example.minigames.dao;

import com.example.minigames.model.GameSession;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSessionDao {

    public void insert(Connection c, long userId, String game, BigDecimal bet, BigDecimal payout, String result) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO game_sessions (user_id, game, bet, payout, result) VALUES (?,?,?,?,?)")) {
            ps.setLong(1, userId);
            ps.setString(2, game);
            ps.setBigDecimal(3, bet);
            ps.setBigDecimal(4, payout);
            ps.setString(5, result);
            ps.executeUpdate();
        }
    }

    public List<GameSession> findByUser(Connection c, long userId) throws SQLException {
        return query(c, "SELECT id, user_id, game, bet, payout, result, created_at " +
                "FROM game_sessions WHERE user_id = ? ORDER BY created_at DESC", userId);
    }

    public List<GameSession> findAllRecent(Connection c, int limit) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id, user_id, game, bet, payout, result, created_at " +
                        "FROM game_sessions ORDER BY created_at DESC LIMIT ?")) {
            ps.setInt(1, limit);
            return read(ps);
        }
    }

    private List<GameSession> query(Connection c, String sql, long arg) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, arg);
            return read(ps);
        }
    }

    private List<GameSession> read(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            List<GameSession> list = new ArrayList<>();
            while (rs.next()) {
                GameSession g = new GameSession();
                g.setId(rs.getLong("id"));
                g.setUserId(rs.getLong("user_id"));
                g.setGame(rs.getString("game"));
                g.setBet(rs.getBigDecimal("bet"));
                g.setPayout(rs.getBigDecimal("payout"));
                g.setResult(rs.getString("result"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) g.setCreatedAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
                list.add(g);
            }
            return list;
        }
    }
}

package com.example.minigames.dao;

import com.example.minigames.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    public Optional<User> findByUsername(Connection c, String username) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id, username, password_hash, role, balance, is_active, created_at " +
                        "FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<User> findById(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id, username, password_hash, role, balance, is_active, created_at " +
                        "FROM users WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<User> lockById(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id, username, password_hash, role, balance, is_active, created_at " +
                        "FROM users WHERE id = ? FOR UPDATE")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public long create(Connection c, String username, String passwordHash, String role) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users (username, password_hash, role) VALUES (?,?,?) RETURNING id")) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, role);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public void updateBalance(Connection c, long userId, BigDecimal newBalance) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE users SET balance = ? WHERE id = ?")) {
            ps.setBigDecimal(1, newBalance);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void setActive(Connection c, long userId, boolean active) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE users SET is_active = ? WHERE id = ?")) {
            ps.setBoolean(1, active);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public List<User> search(Connection c, String query) throws SQLException {
        String sql = "SELECT id, username, password_hash, role, balance, is_active, created_at FROM users";
        boolean hasQuery = query != null && !query.isBlank();
        if (hasQuery) sql += " WHERE username ILIKE ?";
        sql += " ORDER BY id";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (hasQuery) ps.setString(1, "%" + query.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<User> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setBalance(rs.getBigDecimal("balance"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toInstant().atOffset(java.time.ZoneOffset.UTC));
        return u;
    }
}

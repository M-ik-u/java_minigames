package com.example.minigames.service;

import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserDao users = new UserDao();

    public User register(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || username.length() < 3) {
            throw new IllegalArgumentException("Имя пользователя должно содержать не менее 3 символов");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать не менее 6 символов");
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try (Connection c = Db.conn()) {
            if (users.findByUsername(c, username).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким именем уже существует");
            }
            long id = users.create(c, username, hash, "USER");
            return users.findById(c, id).orElseThrow();
        }
    }

    public Optional<User> login(String username, String password) throws SQLException {
        try (Connection c = Db.conn()) {
            Optional<User> ou = users.findByUsername(c, username);
            if (ou.isEmpty()) return Optional.empty();
            User u = ou.get();
            if (!u.isActive()) return Optional.empty();
            if (!BCrypt.checkpw(password, u.getPasswordHash())) return Optional.empty();
            return Optional.of(u);
        }
    }
}

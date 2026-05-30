package com.example.minigames.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class Db {
    private static final DataSource DS = build();

    private Db() {}

    private static DataSource build() {
        String url = env("JDBC_URL", "jdbc:postgresql://localhost:5432/minigames");
        String user = env("DB_USER", "minigames");
        String pass = env("DB_PASSWORD", "minigames");

        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setMaximumPoolSize(10);
        cfg.setPoolName("minigames-pool");
        cfg.setAutoCommit(true);
        // Не пытаемся открыть соединение в конструкторе пула — БД может быть ещё не готова.
        cfg.setInitializationFailTimeout(-1);
        return new HikariDataSource(cfg);
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    public static DataSource ds() { return DS; }

    public static Connection conn() throws SQLException { return DS.getConnection(); }
}

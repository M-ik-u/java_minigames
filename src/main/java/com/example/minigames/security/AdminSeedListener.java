package com.example.minigames.security;

import com.example.minigames.dao.UserDao;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

@WebListener
public class AdminSeedListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AdminSeedListener.class);
    private final UserDao users = new UserDao();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection c = Db.conn()) {
            if (users.findByUsername(c, "admin").isEmpty()) {
                String hash = BCrypt.hashpw("admin123", BCrypt.gensalt(10));
                long id = users.create(c, "admin", hash, "ADMIN");
                log.info("Создан администратор по умолчанию: admin/admin123 (id={})", id);
            }
        } catch (Throwable t) {
            // Печатаем напрямую — иначе при Error логгер может не успеть.
            System.err.println("[AdminSeedListener] FAILED:");
            t.printStackTrace(System.err);
            // Не пробрасываем дальше — пусть приложение поднимется и можно будет диагностировать через UI.
        }
    }
}

package com.example.minigames.security;

import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC = Set.of(
            "/", "/index.jsp", "/login", "/register"
    );

    private final UserDao users = new UserDao();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse w = (HttpServletResponse) res;

        String path = r.getRequestURI().substring(r.getContextPath().length());

        if (path.startsWith("/static") || path.startsWith("/css") || path.startsWith("/js")
                || PUBLIC.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = r.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            w.sendRedirect(r.getContextPath() + "/login");
            return;
        }

        // FIX: проверяем актуальный статус пользователя из БД при каждом запросе.
        // Объект User в сессии может быть устаревшим — администратор мог заблокировать
        // пользователя уже после того, как тот залогинился. Без этой проверки
        // заблокированный продолжает пользоваться сайтом до истечения сессии.
        try (Connection c = Db.conn()) {
            Optional<User> fresh = users.findById(c, user.getId());
            if (fresh.isEmpty() || !fresh.get().isActive()) {
                // Инвалидируем сессию и отправляем на логин
                session.invalidate();
                w.sendRedirect(r.getContextPath() + "/login?blocked=1");
                return;
            }
            // Обновляем объект в сессии (баланс и другие поля тоже будут свежими)
            session.setAttribute("user", fresh.get());
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (path.startsWith("/admin") && !user.isAdmin()) {
            w.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
            return;
        }

        chain.doFilter(req, res);
    }
}

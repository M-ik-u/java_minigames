package com.example.minigames.controller;

import com.example.minigames.dao.GameSessionDao;
import com.example.minigames.dao.TransactionDao;
import com.example.minigames.dao.UserDao;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet({"/admin/users", "/admin/stats", "/admin/sessions", "/admin/toggle"})
public class AdminServlet extends HttpServlet {
    private final UserDao users = new UserDao();
    private final TransactionDao transactions = new TransactionDao();
    private final GameSessionDao sessions = new GameSessionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try (Connection c = Db.conn()) {
            switch (path) {
                case "/admin/users" -> {
                    String q = req.getParameter("q");
                    req.setAttribute("q", q);
                    req.setAttribute("users", users.search(c, q));
                    req.getRequestDispatcher("/WEB-INF/jsp/admin_users.jsp").forward(req, resp);
                }
                case "/admin/stats" -> {
                    req.setAttribute("userCount", users.search(c, null).size());
                    req.setAttribute("turnover", transactions.totalTurnover(c));
                    req.getRequestDispatcher("/WEB-INF/jsp/admin_stats.jsp").forward(req, resp);
                }
                case "/admin/sessions" -> {
                    req.setAttribute("sessions", sessions.findAllRecent(c, 200));
                    req.getRequestDispatcher("/WEB-INF/jsp/admin_sessions.jsp").forward(req, resp);
                }
                default -> resp.sendError(404);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!"/admin/toggle".equals(req.getServletPath())) {
            resp.sendError(404);
            return;
        }
        try (Connection c = Db.conn()) {
            long id = Long.parseLong(req.getParameter("id"));
            // FIX: читаем актуальный статус из БД и инвертируем его.
            // Нельзя доверять значению из формы — EL не вычисляет !boolean корректно
            // в атрибуте value, поэтому туда всегда приходила строка "false".
            boolean currentlyActive = users.findById(c, id)
                    .map(u -> u.isActive())
                    .orElse(true);
            users.setActive(c, id, !currentlyActive);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}

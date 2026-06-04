package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.service.AuthService;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // FIX: показываем внятное сообщение если пользователь был заблокирован
        // (AuthFilter перенаправляет сюда с параметром ?blocked=1)
        if ("1".equals(req.getParameter("blocked"))) {
            req.setAttribute("error", "Ваш аккаунт заблокирован. Обратитесь к администратору.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            // FIX: различаем "заблокирован" и "неверный пароль" —
            // проверяем существование пользователя отдельно от проверки пароля
            Optional<User> ou = auth.login(username, password);
            if (ou.isEmpty()) {
                // Определяем причину отказа: пользователь существует, но заблокирован?
                try (Connection c = Db.conn()) {
                    Optional<User> found = users.findByUsername(c, username);
                    if (found.isPresent() && !found.get().isActive()) {
                        req.setAttribute("error", "Ваш аккаунт заблокирован. Обратитесь к администратору.");
                    } else {
                        req.setAttribute("error", "Неверный логин или пароль");
                    }
                }
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                return;
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("user", ou.get());
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

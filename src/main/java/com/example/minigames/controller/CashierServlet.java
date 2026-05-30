package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.service.BalanceService;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/cashier")
public class CashierServlet extends HttpServlet {
    private final BalanceService balance = new BalanceService();
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/cashier.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            if ("deposit".equals(action)) {
                balance.deposit(u.getId(), amount);
                req.setAttribute("ok", "Счёт пополнен на " + amount);
            } else if ("withdraw".equals(action)) {
                balance.withdraw(u.getId(), amount);
                req.setAttribute("ok", "Выведено " + amount);
            }
            refreshUser(req, u);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Введите корректную сумму");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/cashier.jsp").forward(req, resp);
    }

    private void refreshUser(HttpServletRequest req, User u) throws SQLException {
        try (Connection c = Db.conn()) {
            User fresh = users.findById(c, u.getId()).orElse(u);
            req.getSession().setAttribute("user", fresh);
        }
    }
}

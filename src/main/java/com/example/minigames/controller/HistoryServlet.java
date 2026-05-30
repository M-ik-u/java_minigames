package com.example.minigames.controller;

import com.example.minigames.dao.GameSessionDao;
import com.example.minigames.dao.TransactionDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    private final TransactionDao transactions = new TransactionDao();
    private final GameSessionDao sessions = new GameSessionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        try (Connection c = Db.conn()) {
            req.setAttribute("txs", transactions.findByUser(c, u.getId()));
            req.setAttribute("games", sessions.findByUser(c, u.getId()));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/history.jsp").forward(req, resp);
    }
}

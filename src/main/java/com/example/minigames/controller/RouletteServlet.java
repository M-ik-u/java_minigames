package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.game.RouletteEngine;
import com.example.minigames.game.RouletteEngine.Bet;
import com.example.minigames.game.RouletteEngine.BetType;
import com.example.minigames.model.User;
import com.example.minigames.service.GameTxService;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/game/roulette")
public class RouletteServlet extends HttpServlet {
    private final RouletteEngine engine = new RouletteEngine();
    private final GameTxService tx = new GameTxService();
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/roulette.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        try {
            List<Bet> bets = new ArrayList<>();
            addBet(bets, BetType.RED, req.getParameter("amount_red"), null);
            addBet(bets, BetType.BLACK, req.getParameter("amount_black"), null);
            addBet(bets, BetType.EVEN, req.getParameter("amount_even"), null);
            addBet(bets, BetType.ODD, req.getParameter("amount_odd"), null);
            String numAmt = req.getParameter("amount_number");
            String numStr = req.getParameter("number");
            if (numAmt != null && !numAmt.isBlank() && numStr != null && !numStr.isBlank()) {
                int n = Integer.parseInt(numStr);
                if (n < 0 || n > 36) throw new IllegalArgumentException("Номер должен быть в диапазоне 0..36");
                addBet(bets, BetType.NUMBER, numAmt, n);
            }
            if (bets.isEmpty()) throw new IllegalArgumentException("Не сделано ни одной ставки");

            RouletteEngine.Outcome out = engine.spin(bets);
            String result = "Выпало " + out.number + " (" + (out.number == 0 ? "ZERO"
                    : (RouletteEngine.isRed(out.number) ? "RED" : "BLACK")) + ")";
            tx.playMultiBet(u.getId(), "ROULETTE", out.totalBet, out.totalPayout, result);
            req.setAttribute("outcome", out);
            req.setAttribute("isRed", RouletteEngine.isRed(out.number));
            refreshUser(req, u);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Введите корректные числа");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/roulette.jsp").forward(req, resp);
    }

    private void addBet(List<Bet> bets, BetType t, String amt, Integer number) {
        if (amt == null || amt.isBlank()) return;
        BigDecimal v = new BigDecimal(amt);
        if (v.signum() <= 0) return;
        bets.add(new Bet(t, number, v));
    }

    private void refreshUser(HttpServletRequest req, User u) throws SQLException {
        try (Connection c = Db.conn()) {
            User fresh = users.findById(c, u.getId()).orElse(u);
            req.getSession().setAttribute("user", fresh);
        }
    }
}

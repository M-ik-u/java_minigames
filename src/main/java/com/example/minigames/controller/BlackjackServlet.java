package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.game.BlackjackEngine;
import com.example.minigames.game.BlackjackRound;
import com.example.minigames.model.User;
import com.example.minigames.service.GameTxService;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/game/blackjack")
public class BlackjackServlet extends HttpServlet {
    private final BlackjackEngine engine = new BlackjackEngine();
    private final GameTxService tx = new GameTxService();
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // При входе на страницу — сбрасываем сохранённую ставку к 10
        req.getSession().removeAttribute("bjLastBet");
        forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "deal" -> startRound(req, u);
                case "hit"  -> hit(req);
                case "stand" -> stand(req);
                case "double" -> doubleDown(req, u);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Введите корректную ставку");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlackjackRound r = (BlackjackRound) req.getSession().getAttribute("bj");
        req.setAttribute("round", r);
        req.getRequestDispatcher("/WEB-INF/jsp/blackjack.jsp").forward(req, resp);
    }

    private void startRound(HttpServletRequest req, User u) throws SQLException {
        BigDecimal bet = new BigDecimal(req.getParameter("bet"));
        if (bet.signum() <= 0) throw new IllegalArgumentException("Ставка должна быть положительной");
        if (u.getBalance().compareTo(bet) < 0) throw new IllegalArgumentException("Недостаточно средств");

        // FIX: сохраняем ставку в сессию
        req.getSession().setAttribute("bjLastBet", bet);

        BlackjackRound r = new BlackjackRound(engine.newDeck(), bet);
        r.player.add(r.draw());
        r.dealer.add(r.draw());
        r.player.add(r.draw());
        r.dealer.add(r.draw());

        if (BlackjackEngine.isBlackjack(r.player)) {
            if (BlackjackEngine.isBlackjack(r.dealer)) {
                finish(req, u, r, "PUSH", r.bet);
            } else {
                BigDecimal win = r.bet.multiply(new BigDecimal("2.5")).setScale(2, RoundingMode.HALF_UP);
                finish(req, u, r, "BJ", win);
            }
        } else {
            req.getSession().setAttribute("bj", r);
        }
    }

    private void hit(HttpServletRequest req) {
        BlackjackRound r = require(req);
        r.player.add(r.draw());
        if (r.playerScore() > 21) {
            try { finish(req, currentUser(req), r, "LOSE", BigDecimal.ZERO); }
            catch (SQLException e) { throw new RuntimeException(e); }
        }
    }

    private void stand(HttpServletRequest req) throws SQLException {
        BlackjackRound r = require(req);
        engine.dealerPlay(r.dealer, r.deck);
        decide(req, r);
    }

    private void doubleDown(HttpServletRequest req, User u) throws SQLException {
        BlackjackRound r = require(req);
        if (r.player.size() != 2) throw new IllegalArgumentException("Дабл доступен только на первых двух картах");
        BigDecimal newBet = r.bet.multiply(BigDecimal.valueOf(2));
        if (u.getBalance().compareTo(newBet) < 0) throw new IllegalArgumentException("Недостаточно средств для дабла");
        r.bet = newBet;
        r.doubled = true;
        r.player.add(r.draw());
        if (r.playerScore() > 21) {
            finish(req, u, r, "LOSE", BigDecimal.ZERO);
        } else {
            engine.dealerPlay(r.dealer, r.deck);
            decide(req, r);
        }
    }

    private void decide(HttpServletRequest req, BlackjackRound r) throws SQLException {
        int p = r.playerScore(), d = r.dealerScore();
        String outcome;
        BigDecimal payout;
        if (d > 21 || p > d) {
            outcome = "WIN";
            payout = r.bet.multiply(BigDecimal.valueOf(2));
        } else if (p == d) {
            outcome = "PUSH";
            payout = r.bet;
        } else {
            outcome = "LOSE";
            payout = BigDecimal.ZERO;
        }
        finish(req, currentUser(req), r, outcome, payout);
    }

    private void finish(HttpServletRequest req, User u, BlackjackRound r, String outcome, BigDecimal payout) throws SQLException {
        r.outcome = outcome;
        r.payout = payout;
        r.finished = true;
        String result = "P=" + r.playerScore() + " D=" + r.dealerScore() + " " + outcome;
        tx.playSingleBet(u.getId(), "BLACKJACK", r.bet, payout, result);
        refreshUser(req, u);
    }

    private BlackjackRound require(HttpServletRequest req) {
        BlackjackRound r = (BlackjackRound) req.getSession().getAttribute("bj");
        if (r == null || r.finished) throw new IllegalArgumentException("Активной партии нет");
        return r;
    }

    private User currentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    private void refreshUser(HttpServletRequest req, User u) throws SQLException {
        try (Connection c = Db.conn()) {
            User fresh = users.findById(c, u.getId()).orElse(u);
            req.getSession().setAttribute("user", fresh);
        }
    }
}

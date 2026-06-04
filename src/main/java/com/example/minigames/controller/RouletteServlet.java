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
        req.getSession().removeAttribute("roulLastBets");
        req.getRequestDispatcher("/WEB-INF/jsp/roulette.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        try {
            String amtRed    = req.getParameter("amount_red");
            String amtBlack  = req.getParameter("amount_black");
            String amtEven   = req.getParameter("amount_even");
            String amtOdd    = req.getParameter("amount_odd");
            String amtNumber = req.getParameter("amount_number");
            String numStr    = req.getParameter("number");

            req.getSession().setAttribute("roulLastBets",
                    new RouletteLastBets(amtRed, amtBlack, amtEven, amtOdd, amtNumber, numStr));

            List<Bet> bets = new ArrayList<>();
            addBet(bets, BetType.RED,   amtRed,   null);
            addBet(bets, BetType.BLACK, amtBlack, null);
            addBet(bets, BetType.EVEN,  amtEven,  null);
            addBet(bets, BetType.ODD,   amtOdd,   null);
            if (amtNumber != null && !amtNumber.isBlank() && numStr != null && !numStr.isBlank()) {
                int n = Integer.parseInt(numStr);
                if (n < 0 || n > 36) throw new IllegalArgumentException("Номер должен быть в диапазоне 0..36");
                addBet(bets, BetType.NUMBER, amtNumber, n);
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

    /**
     * FIX: поля должны быть private + геттеры, иначе EL (Jakarta Expression Language)
     * не может найти свойство — он ищет методы getRed(), getBlack() и т.д.,
     * а не обращается напрямую к public-полям.
     */
    public static class RouletteLastBets {
        private final String red;
        private final String black;
        private final String even;
        private final String odd;
        private final String amountNumber;
        private final String number;

        public RouletteLastBets(String red, String black, String even, String odd,
                                String amountNumber, String number) {
            this.red          = nvl(red);
            this.black        = nvl(black);
            this.even         = nvl(even);
            this.odd          = nvl(odd);
            this.amountNumber = nvl(amountNumber);
            this.number       = nvl(number);
        }

        public String getRed()          { return red; }
        public String getBlack()        { return black; }
        public String getEven()         { return even; }
        public String getOdd()          { return odd; }
        public String getAmountNumber() { return amountNumber; }
        public String getNumber()       { return number; }

        private static String nvl(String s) { return s == null ? "" : s; }
    }
}

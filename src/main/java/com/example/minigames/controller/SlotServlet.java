package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.game.SlotEngine;
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

@WebServlet("/game/slot")
public class SlotServlet extends HttpServlet {
    private final SlotEngine engine = new SlotEngine();
    private final GameTxService tx = new GameTxService();
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // При входе на страницу — сбрасываем сохранённую ставку к 10
        req.getSession().removeAttribute("slotLastBet");
        req.getRequestDispatcher("/WEB-INF/jsp/slot.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        try {
            BigDecimal bet = new BigDecimal(req.getParameter("bet"));
            // FIX: сохраняем ставку в сессию, чтобы JSP мог подставить её обратно
            req.getSession().setAttribute("slotLastBet", bet);

            SlotEngine.Spin spin = engine.spin(bet);
            String result = String.join(" | ",
                    spin.reels[0].name(), spin.reels[1].name(), spin.reels[2].name());
            tx.playSingleBet(u.getId(), "SLOT", bet, spin.payout, result);
            req.setAttribute("reels", spin.reels);
            req.setAttribute("payout", spin.payout);
            refreshUser(req, u);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Введите корректную ставку");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/slot.jsp").forward(req, resp);
    }

    private void refreshUser(HttpServletRequest req, User u) throws SQLException {
        try (Connection c = Db.conn()) {
            User fresh = users.findById(c, u.getId()).orElse(u);
            req.getSession().setAttribute("user", fresh);
        }
    }
}

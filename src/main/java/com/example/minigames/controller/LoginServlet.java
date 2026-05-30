package com.example.minigames.controller;

import com.example.minigames.model.User;
import com.example.minigames.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            Optional<User> ou = auth.login(username, password);
            if (ou.isEmpty()) {
                req.setAttribute("error", "Неверный логин или пароль");
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

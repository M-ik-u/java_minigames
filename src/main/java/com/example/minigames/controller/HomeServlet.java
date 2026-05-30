package com.example.minigames.controller;

import com.example.minigames.dao.UserDao;
import com.example.minigames.model.User;
import com.example.minigames.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        try (Connection c = Db.conn()) {
            User fresh = users.findById(c, u.getId()).orElse(u);
            req.getSession().setAttribute("user", fresh);
            req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

package com.example.minigames.security;

import com.example.minigames.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC = Set.of(
            "/", "/index.jsp", "/login", "/register"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse w = (HttpServletResponse) res;

        String path = r.getRequestURI().substring(r.getContextPath().length());

        if (path.startsWith("/static") || path.startsWith("/css") || path.startsWith("/js")
                || PUBLIC.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = r.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            w.sendRedirect(r.getContextPath() + "/login");
            return;
        }

        if (path.startsWith("/admin") && !user.isAdmin()) {
            w.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
            return;
        }

        chain.doFilter(req, res);
    }
}

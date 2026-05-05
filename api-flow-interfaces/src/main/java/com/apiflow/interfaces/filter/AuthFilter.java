package com.apiflow.interfaces.filter;

import com.apiflow.application.user.AuthService;
import com.apiflow.application.user.dto.UserDTO;
import com.apiflow.interfaces.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 放行静态资源
        if (path.startsWith(contextPath + "/static/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 放行登录相关
        if (path.equals(contextPath + "/") ||
                path.equals(contextPath) ||
                path.equals(contextPath + "/login") ||
                path.equals(contextPath + "/auth/login") ||
                path.equals(contextPath + "/logout") ||
                path.equals(contextPath + "/index")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = TokenUtil.getTokenFromCookie(request);

        if (token == null || !authService.isValidToken(token)) {
            response.sendRedirect(contextPath + "/login");
            return;
        }

        UserDTO user = authService.getUserByToken(token);
        if (user == null) {
            response.sendRedirect(contextPath + "/login");
            return;
        }

        request.setAttribute("username", user.getUsername());
        request.setAttribute("role", user.getRole());
        request.setAttribute("token", token);

        authService.refreshToken(token);

        filterChain.doFilter(request, response);
    }

}
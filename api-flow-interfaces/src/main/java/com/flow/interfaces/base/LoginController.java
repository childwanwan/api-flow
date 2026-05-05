package com.flow.interfaces.base;

import com.flow.application.auth.AuthService;
import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.common.result.Result;
import com.flow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token != null && authService.isValidToken(token)) {
            return "redirect:/index";
        }
        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("error", "用户名或密码错误");
        }
        String logout = request.getParameter("logout");
        if (logout != null) {
            request.setAttribute("logout", "已成功退出登录");
        }
        return "login";
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        String token = authService.login(username, password);
        if (token == null) {
            return Result.fail("AUTH001", "用户名或密码错误", null);
        }
        
        Cookie cookie = new Cookie(TokenUtil.AUTH_TOKEN, token);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
        
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("token", token);
        return Result.success(data);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String token = TokenUtil.getTokenFromCookie(request);
        authService.logout(token);
        
        Cookie cookie = new Cookie(TokenUtil.AUTH_TOKEN, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return "redirect:/login?logout";
    }

    @GetMapping("/auth/info")
    @ResponseBody
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token == null) {
            return Result.fail("AUTH002", "未登录", null);
        }
        String username = authService.getUsernameByToken(token);
        if (username == null) {
            return Result.fail("AUTH002", "未登录", null);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        return Result.success(data);
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token == null || !authService.isValidToken(token)) {
            return "redirect:/login";
        }
        UserIDTO user = authService.getUserByToken(token);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        return "index";
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token != null && authService.isValidToken(token)) {
            return "redirect:/index";
        }
        return "redirect:/login";
    }
}
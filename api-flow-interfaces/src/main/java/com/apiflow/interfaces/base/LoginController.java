package com.apiflow.interfaces.base;

import com.apiflow.application.user.AuthService;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.base.request.LoginRequest;
import com.apiflow.interfaces.base.response.LoginResponse;
import com.apiflow.interfaces.util.TokenUtil;
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
    public Result<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String username = request.getUsername();
        String password = request.getPassword();
        String token = authService.login(username, password);
        if (token == null) {
            return Result.fail(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }
        
        Cookie cookie = new Cookie(TokenUtil.AUTH_TOKEN, token);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
        
        LoginResponse loginResponse = LoginResponse.builder()
                .username(username)
                .token(token)
                .build();
        return Result.success(loginResponse);
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
            return Result.fail(ErrorCode.AUTH_NOT_LOGGED_IN);
        }
        String username = authService.getUsernameByToken(token);
        if (username == null) {
            return Result.fail(ErrorCode.AUTH_NOT_LOGGED_IN);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        return Result.success(data);
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("username", request.getAttribute("username"));
        model.addAttribute("role", request.getAttribute("role"));
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        model.addAttribute("username", request.getAttribute("username"));
        return "dashboard";
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

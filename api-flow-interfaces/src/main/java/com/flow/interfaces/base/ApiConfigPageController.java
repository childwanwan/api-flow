package com.flow.interfaces.base;

import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.application.auth.AuthService;
import com.flow.interfaces.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api-config")
public class ApiConfigPageController {

    private final AuthService authService;

    @GetMapping
    public String apiConfigPage(Model model, HttpServletRequest request) {
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
        return "api-config";
    }

}
package com.apiflow.interfaces.base;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api-group")
public class ApiGroupPageController {

    @GetMapping
    public String apiGroupPage(Model model, HttpServletRequest request) {
        model.addAttribute("username", request.getAttribute("username"));
        model.addAttribute("role", request.getAttribute("role"));
        return "api-group";
    }
}

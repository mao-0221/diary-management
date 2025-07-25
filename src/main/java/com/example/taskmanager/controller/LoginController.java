package com.example.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                 Model model) {
        if (error != null) {
            model.addAttribute("loginError", "ユーザー名またはパスワードが間違っています");
        }
        return "login"; // templates/login.html を表示
    }
}

package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/connections/add")
    public String connectionsAdd() {
        return "connections_add";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            return "redirect:/login";
        }

        var userEntity = principal.getUser();
        model.addAttribute("user", userEntity);
        return "profile";
    }
}


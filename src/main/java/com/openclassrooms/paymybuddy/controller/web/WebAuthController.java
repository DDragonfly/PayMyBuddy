package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.dto.RegisterRequest;
import com.openclassrooms.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Validated
public class WebAuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String doRegister(@ModelAttribute @Valid RegisterRequest form) {
        userService.register(form.email(), form.username(), form.password());
        return "redirect:/register?success=1";
    }

}

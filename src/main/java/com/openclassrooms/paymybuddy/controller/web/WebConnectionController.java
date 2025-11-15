package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.security.CustomUserDetails;
import com.openclassrooms.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebConnectionController {

    private final UserService userService;

    @PostMapping("/connections/add")
    public String addConnection(@RequestParam("email") String friendEmail, Authentication authentication, RedirectAttributes redirectAttributes) {

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            return "redirect:/login";
        }

        var currentUser = principal.getUser();

        try {
            userService.addConnectionByEmail(currentUser.getUserId(), friendEmail);
            redirectAttributes.addFlashAttribute("success", "Relation ajoutée avec succès.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/connections/add";
    }


}

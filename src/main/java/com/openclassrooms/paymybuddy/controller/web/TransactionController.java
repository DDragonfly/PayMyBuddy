package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.security.AppUserDetails;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;

    public record ConnectionOption(Integer id, String name) {}
    public record TransferRow(String connectionName, String description, BigDecimal amount) {}

    @GetMapping("/transfer")
    public String showTransfer(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            return "redirect:/login";
        }

        var currentUser = principal.getUser();

        var friends = userService.listFriends(currentUser.getUserId())
                .stream()
                .map(u -> new ConnectionOption(u.getUserId(), u.getUsername()))
                .toList();

        model.addAttribute("connections", friends);

        var lastTransactions = transactionService.findLastForUser(currentUser.getUserId())
                .stream()
                .map(t -> new TransferRow(
                        t.getReceiver().getUsername(),   // nome della relazione
                        t.getDescription(),
                        t.getAmount()
                ))
                .toList();

        model.addAttribute("transfers", lastTransactions);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String doTransfer(@RequestParam("connectionId") Integer receiverId,
                             @RequestParam("amount") BigDecimal amount,
                             @RequestParam(value = "description", required = false) String description,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            return "redirect:/login";
        }

        var sender = principal.getUser();

        try {
            transactionService.createTransaction(
                    sender.getUserId(),
                    receiverId,
                    description,
                    amount
            );
            redirectAttributes.addFlashAttribute("success", "Transaction effectué avec succès.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/transfer";
    }
}

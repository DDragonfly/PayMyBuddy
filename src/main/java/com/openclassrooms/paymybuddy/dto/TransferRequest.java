package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Integer senderId,
        @NotNull Integer receiverId,
        String description,
        @NotNull @Positive BigDecimal amount) {
}

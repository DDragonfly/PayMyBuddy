package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Integer senderId,
        @NotNull Integer receiverId,
        @Size(max = 200) String description,
        @NotNull @Positive BigDecimal amount
) {}

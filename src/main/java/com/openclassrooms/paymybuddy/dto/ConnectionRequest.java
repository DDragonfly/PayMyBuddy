package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;

public record ConnectionRequest(
        @NotNull Integer ownerId,
        @NotNull Integer friendId
) {}

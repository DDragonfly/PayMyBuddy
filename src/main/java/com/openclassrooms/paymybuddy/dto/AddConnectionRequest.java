package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;

public record AddConnectionRequest(
        @NotNull Integer ownerId,
        @NotNull Integer friendId
) {}

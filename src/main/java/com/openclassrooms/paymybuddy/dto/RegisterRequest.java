package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email String email,
        @Size(min = 5, max = 100) String username,
        @Size(min = 8, max = 100) String password
) {}

package com.openclassrooms.paymybuddy.dto;

import java.math.BigDecimal;

public record TransferResponse(Integer id, String description, BigDecimal amount, BigDecimal fee, String createdAt) {
}

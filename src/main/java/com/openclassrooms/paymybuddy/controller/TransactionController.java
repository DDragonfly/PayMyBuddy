package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransferRequest;
import com.openclassrooms.paymybuddy.dto.TransferResponse;
import com.openclassrooms.paymybuddy.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransferResponse> create(@Valid @RequestBody TransferRequest request) {
        var ts = transactionService.createTransaction(
                request.senderId(),
                request.receiverId(),
                request.description(),
                request.amount()
        );
        var response = new TransferResponse(
                ts.getId(),
                ts.getDescription(),
                ts.getAmount(),
                ts.getFee(),
                ts.getCreatedAt() != null ? ts.getCreatedAt().toString() : null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

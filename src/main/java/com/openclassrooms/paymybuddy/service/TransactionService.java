package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.BusinessException;
import com.openclassrooms.paymybuddy.infrastructure.persistence.TransactionEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository users;

    private static final BigDecimal FEE_RATE = new BigDecimal("0,005");

    @Transactional
    public TransactionEntity createTransaction(Integer senderId, Integer receiverId, String description, BigDecimal amount) {
        if (senderId.equals(receiverId)) throw new BusinessException("Sender and Receiver must differ");
        if (amount == null || amount.signum() <= 0) throw new BusinessException("Amount must be > 0");

        var sender = users.findById(senderId).orElseThrow(() -> new ChangeSetPersister.NotFoundException("Sender not found"));
        var receiver = users.findById(receiverId).orElseThrow(() -> new ChangeSetPersister.NotFoundException("Receiver not found"));

        var fee = amount.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);

        var tx = TransactionEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .description(description)
                .amount(amount.setScale(2, RoundingMode.HALF_UP))
                .fee(fee)
                .build();

        return transactionRepository.save(tx);
    }
}

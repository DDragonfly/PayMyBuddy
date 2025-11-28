package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class TransactionServiceIT {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createTransaction_shouldPersistAndComputeFree() {
        // GIVEN
        Integer senderId = 1;
        Integer receiverId = 2;
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Integration test";

        assertThat(userRepository.findById(senderId)).isPresent();
        assertThat(userRepository.findById(receiverId)).isPresent();

        // WHEN
        TransactionEntity ts = transactionService.createTransaction(senderId, receiverId, description, amount);

        // THEN
        assertThat(ts.getId()).isNotNull();
        assertThat(ts.getAmount()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(ts.getFee()).isEqualByComparingTo(new BigDecimal("0.05"));

        var fromDb = transactionRepository.findById(ts.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getDescription()).isEqualTo(description);
    }
}

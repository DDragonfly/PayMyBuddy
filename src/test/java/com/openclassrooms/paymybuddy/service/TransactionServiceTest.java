package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.BusinessException;
import com.openclassrooms.paymybuddy.exception.NotFoundException;
import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void createTransaction_createsValidTransaction_withCorrectFee() {
        // given
        Integer senderId = 1;
        Integer receiverId = 2;
        String description = "Restaurant";
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expectedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFee = expectedAmount
                .multiply(new BigDecimal("0.005"))
                .setScale(2, RoundingMode.HALF_UP);

        UserEntity sender = UserEntity.builder().userId(senderId).email("sender@mail.com").build();
        UserEntity receiver = UserEntity.builder().userId(receiverId).email("receiver@mail.com").build();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(invocation -> {
                    TransactionEntity t = invocation.getArgument(0);
                    t.setId(10);
                    return t;
                });

        // when
        TransactionEntity result = transactionService.createTransaction(senderId, receiverId, description, amount);

        // then
        assertNotNull(result.getId());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(description, result.getDescription());
        assertEquals(expectedAmount, result.getAmount());
        assertEquals(expectedFee, result.getFee());

        verify(userRepository).findById(senderId);
        verify(userRepository).findById(receiverId);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void createTransaction_rejects_whenSenderEqualsReceiver() {
        // given
        Integer senderId = 1;
        BigDecimal amount = new BigDecimal("10.00");

        // when
        assertThrows(BusinessException.class,
                () -> transactionService.createTransaction(senderId, senderId, "Test", amount));

        // then
        verifyNoInteractions(userRepository, transactionRepository);
    }

    @Test
    void createTransaction_rejects_whenAmountIsNullOrZeroOrNegative() {
        assertThrows(BusinessException.class, () -> transactionService.createTransaction(1, 2, "Test", null));

        assertThrows(BusinessException.class, () -> transactionService.createTransaction(1, 2, "Test", BigDecimal.ZERO));

        assertThrows(BusinessException.class, () -> transactionService.createTransaction(1, 2, "Test", new BigDecimal("-5")));

        verifyNoInteractions(userRepository, transactionRepository);
    }

    @Test
    void createTransaction_throwsException_whenSenderNotFound() {
        // given
        Integer senderId = 1;
        Integer receiverId = 2;
        BigDecimal amount = new BigDecimal("10.00");

        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(senderId, receiverId, "Test", amount));

        verify(userRepository).findById(senderId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(transactionRepository);

    }
    @Test
    void createTransaction_throwsException_whenReceiverNotFound() {
        // given
        Integer senderId = 1;
        Integer receiverId = 2;
        BigDecimal amount = new BigDecimal("10.00");

        UserEntity sender = UserEntity.builder().userId(senderId).build();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.createTransaction(senderId, receiverId, "Test", amount));

        verify(userRepository).findById(senderId);
        verify(userRepository).findById(receiverId);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void createTransaction_withFractionalAmount_calculatesFeeCorrectly() {
        // given
        Integer senderId = 1;
        Integer receiverId = 2;
        String description = "Restaurant not round";
        BigDecimal amount = new BigDecimal("254.79");

        BigDecimal expectedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedFee = expectedAmount
                .multiply(new BigDecimal("0.005"))
                .setScale(2, RoundingMode.HALF_UP);

        UserEntity sender = UserEntity.builder().userId(senderId).email("sencer@mail.com").build();
        UserEntity receiver = UserEntity.builder().userId(receiverId).email("receiver@mail.com").build();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(invocation -> {
                    TransactionEntity t = invocation.getArgument(0);
                    t.setId(10);
                    return t;
                });

        // when
        TransactionEntity result = transactionService.createTransaction(senderId, receiverId, description, amount);

        // then
        assertNotNull(result.getId());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(description, result.getDescription());
        assertEquals(expectedAmount, result.getAmount());
        assertEquals(expectedFee, result.getFee());

        verify(userRepository).findById(senderId);
        verify(userRepository).findById(receiverId);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }
}

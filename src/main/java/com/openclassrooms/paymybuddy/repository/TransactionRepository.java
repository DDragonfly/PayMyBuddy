package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    Page<TransactionEntity> findBySender_UserIdOrReceiver_UserId(Integer senderId, Integer receiverId, Pageable pageable);
    List<TransactionEntity> findTop10BySender_UserIdOrderByCreatedAtDesc(Integer senderId);
}

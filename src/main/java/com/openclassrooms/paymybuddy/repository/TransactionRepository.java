package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.infrastructure.persistence.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {


}

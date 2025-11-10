package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUserId(Integer userId);
}
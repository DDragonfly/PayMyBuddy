package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.infrastructure.persistence.UserConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, Integer> {
    boolean existByUserIdAndConnectionId(Integer userId, Integer connectionId);
}

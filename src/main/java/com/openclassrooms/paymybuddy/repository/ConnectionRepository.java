package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.ConnectionEntity;
import com.openclassrooms.paymybuddy.model.ConnectionId;
import com.openclassrooms.paymybuddy.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, ConnectionId> {

   // boolean existsByUser_UserIdAndConnection_UserId(Integer userId, Integer connectionId);

    @Query("select c.connection from ConnectionEntity c where c.user.userId = :userId")
    List<UserEntity> findFriendsOf(@Param("userId")Integer userId);
}

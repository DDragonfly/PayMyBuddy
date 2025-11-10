package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserConnectionEntity;
import com.openclassrooms.paymybuddy.model.UserConnectionId;
import com.openclassrooms.paymybuddy.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, UserConnectionId> {

    boolean existByUser_UserIdAndConnection_UserId(Integer userId, Integer connectionId);

    @Query("select c.connection from UserConnectionEntity c where c.user.userId = :userId")
    List<UserEntity> findFriendsOf(@Param("userId")Integer userId);
}

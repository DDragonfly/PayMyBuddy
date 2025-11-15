package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.BusinessException;
import com.openclassrooms.paymybuddy.exception.NotFoundException;
import com.openclassrooms.paymybuddy.model.UserConnectionEntity;
import com.openclassrooms.paymybuddy.model.UserConnectionId;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository users;
    private final UserConnectionRepository connections;
    private final PasswordEncoder passwordEncoder;

    public UserEntity register(String email, String username, String rawPassword) {
        if (users.existsByEmail(email)) throw new BusinessException("Email already used");
        var user = UserEntity.builder()
                .email(email)
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .build();
        return users.save(user);
    }

    public void addConnection(Integer ownerId, Integer friendId) {
        if (ownerId.equals(friendId)) throw new BusinessException("Cannot add yourself");
        var owner = users.findById(ownerId).orElseThrow(() -> new NotFoundException("Owner not found"));
        var friend = users.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        var id = new UserConnectionId(owner.getUserId(), friend.getUserId());
        if (connections.existsById(id)) return;
        connections.save(UserConnectionEntity.builder().id(id).user(owner).connection(friend).build());
    }

    public void addConnectionByEmail(Integer ownerId, String friendEmail) {
        var owner = users.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Utilisateur courant introuvable"));

        var friend = users.findByEmail(friendEmail)
                .orElseThrow(() -> new NotFoundException("Aucun utilisateur trouvé avec cet email"));

        addConnection(owner.getUserId(), friend.getUserId());
    }


    public List<UserEntity> listFriends(Integer userId) {
        if (!users.existsById(userId)) throw new NotFoundException("User not found");
        return connections.findFriendsOf(userId);
    }
}

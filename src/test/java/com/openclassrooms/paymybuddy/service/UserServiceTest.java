package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.BusinessException;
import com.openclassrooms.paymybuddy.exception.NotFoundException;
import com.openclassrooms.paymybuddy.model.UserConnectionEntity;
import com.openclassrooms.paymybuddy.model.UserConnectionId;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserConnectionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserConnectionRepository userConnectionRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void register_createsNewUser_whenEmailDoesNotExist() {
        // given
        String email = "test@example.com";
        String username = "TestUser";
        String rawPassword = "TestPassword123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn("HASHED");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity u = invocation.getArgument(0);
            u.setUserId(1);
            return u;
        });

        // when
        UserEntity result = userService.register(email, username, rawPassword);

        // then
        assertNotNull(result.getUserId());
        assertEquals(email, result.getEmail());
        assertEquals(username, result.getUsername());
        assertEquals("HASHED", result.getPasswordHash());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void register_throwsException_whenEmailAlreadyUsed() {
        // given
        String email = "exists@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // then
        assertThrows(BusinessException.class, () -> userService.register(email, "User", "TestPassword123"));

        verify(userRepository).existsByEmail(email);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, userConnectionRepository);
    }

    @Test
    void addConnection_createsConnection_whenValid() {
        // given
        Integer ownerId = 1;
        Integer friendId = 2;

        UserEntity owner = UserEntity.builder().userId(ownerId).build();
        UserEntity friend = UserEntity.builder().userId(friendId).build();

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
        when(userConnectionRepository.existsById(new UserConnectionId(ownerId, friendId))).thenReturn(false);

        // when
        userService.addConnection(ownerId, friendId);

        // then
        verify(userRepository).findById(ownerId);
        verify(userRepository).findById(friendId);
        verify(userConnectionRepository).existsById(new UserConnectionId(ownerId, friendId));
        verify(userConnectionRepository).save(any(UserConnectionEntity.class));
    }

    @Test
    void addConnection_rejects_whenAddingSelf() {
        // given
        Integer ownerId = 1;
        Integer friendId = 1;

        // when
        assertThrows(BusinessException.class, () -> userService.addConnection(ownerId, friendId));

        // then
        verifyNoInteractions(userRepository, userConnectionRepository);
    }

    @Test
    void listFriends_returnsFriendsList() {
        // given
        Integer userId = 1;
        UserEntity f1 = UserEntity.builder().userId(2).username("Aldo").build();
        UserEntity f2 = UserEntity.builder().userId(3).username("Giovanni").build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userConnectionRepository.findFriendsOf(userId)).thenReturn(List.of(f1, f2));

        // when
        var result = userService.listFriends(userId);

        // then
        assertEquals(2, result.size());
        assertEquals("Aldo", result.get(0).getUsername());
        assertEquals("Giovanni", result.get(1).getUsername());

        verify(userRepository).existsById(userId);
        verify(userConnectionRepository).findFriendsOf(userId);
    }

    @Test
    void listFriends_throwsException_whenUserNotFound() {
        // given
        Integer userId = 99;
        when(userRepository.existsById(userId)).thenReturn(false);

        // when
        assertThrows(NotFoundException.class, () -> userService.listFriends(userId));

        // then
        verify(userRepository).existsById(userId);
        verifyNoInteractions(userConnectionRepository);
    }
}

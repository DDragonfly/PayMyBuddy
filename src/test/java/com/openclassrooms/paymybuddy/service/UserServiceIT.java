package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_shouldPersistUserInDatabase() {
        // GIVEN
        String email = "it.test@mail.com";
        String username = "IT-user";
        String password = "password123!";

        //WHEN
        UserEntity saved = userService.register(email, username, password);

        // THEN
        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(email);

        var fromDb = userRepository.findByEmail(email);
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getUsername()).isEqualTo(username);
    }
}

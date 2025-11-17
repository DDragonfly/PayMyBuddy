package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.config.SecurityConfig;
import com.openclassrooms.paymybuddy.exception.BusinessException;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.security.AppUserDetails;
import com.openclassrooms.paymybuddy.security.AppUserDetailsService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConnectionController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class ConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserEntity currentUser;
    private AppUserDetails principal;

    @BeforeEach
    public void setup() {
        currentUser = UserEntity.builder()
                .userId(1)
                .username("monica")
                .email("monica@test.com")
                .passwordHash("Password123")
                .build();

        principal = new AppUserDetails(currentUser);
    }

    @Test
    void addConnection_withValidEmail_shouldCallServiceAndRedirectWithSuccess() throws Exception {
        mockMvc.perform(post("/connections/add")
                        .with(user(principal))
                        .with(csrf())
                        .param("email", "chandler@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections/add"))
                .andExpect(flash().attribute("success", "Relation ajoutée avec succès."));

        verify(userService).addConnectionByEmail(1, "chandler@test.com");
    }

    @Test
    void addConnection_whenServiceThrowsBusinessException_shouldStoreErrorFlash() throws Exception {
        doThrow(new BusinessException("Aucun utilisateur trouvé avec cet email"))
                .when(userService)
                .addConnectionByEmail(1, "chandler@test.com");

        mockMvc.perform(post("/connections/add")
        .with(user(principal))
                .param("email", "chandler@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections/add"))
                .andExpect(flash().attribute("error", "Aucun utilisateur trouvé avec cet email"));
    }

    @Test
    void addConnection_withoutAuthentication_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/connections/add")
                .with(csrf())
                .param("email", "chandler@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}

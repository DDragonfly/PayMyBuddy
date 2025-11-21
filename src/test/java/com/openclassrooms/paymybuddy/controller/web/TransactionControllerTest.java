package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.config.SecurityConfig;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.security.AppUserDetails;
import com.openclassrooms.paymybuddy.security.AppUserDetailsService;
import com.openclassrooms.paymybuddy.service.TransactionService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserEntity currentUser;
    private AppUserDetails principal;

    @BeforeEach
    void setup() {
        currentUser = UserEntity.builder()
                .userId(1)
                .username("monica")
                .email("monica@test.com")
                .passwordHash("password123")
                .build();
        principal = new AppUserDetails(currentUser);
    }

    @Test
    void getTransfer_withAuthenticatedUser_shouldReturnViewAndModel() throws Exception {
        var friend = UserEntity.builder()
                .userId(2)
                .username("chandler")
                .email("chandler@mail.com")
                .passwordHash("x")
                .build();

        when(userService.listFriends(1)).thenReturn(List.of(friend));

        mockMvc.perform(get("/transfer").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("transfers"));
    }

    @Test
    void postTransfer_withValidData_shouldCallServiceAndRedirecdWithSuccess() throws Exception {
        when(userService.listFriends(1)).thenReturn(List.of());

        mockMvc.perform(post("/transfer")
                        .with(user(principal))
                        .with(csrf())
                        .param("connectionId", "2")
                        .param("amount", "10.00")
                        .param("description", "Restaurant"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"))
                .andExpect(flash().attribute("success", "Transaction effectué avec succès."));

        verify(transactionService).createTransaction(
                eq(1),
                eq(2),
                eq("Restaurant"),
                eq(new BigDecimal("10.00"))
        );
    }

    @Test
    void getTransfer_withoutAuthentication_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void transferPage_withSuccessFlash_shouldShowSuccessMessage() throws Exception {
        when(userService.listFriends(1)).thenReturn(List.of());

        mockMvc.perform(get("/transfer")
                        .with(user(principal))
                        .flashAttr("success", "Transfert effectué avec succès."))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(content().string(
                        containsString("Transfert effectué avec succès.")));
    }

    @Test
    void transferPage_withErrorFlash_shouldShowErrorMessage() throws Exception {
        when(userService.listFriends(1)).thenReturn(List.of());

        mockMvc.perform(get("/transfer")
                        .with(user(principal))
                        .flashAttr("error", "Sender and Receiver must differ"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(content().string(
                        containsString("Sender and Receiver must differ")));
    }
}

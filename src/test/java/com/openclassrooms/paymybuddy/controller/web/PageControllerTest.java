package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.config.SecurityConfig;
import com.openclassrooms.paymybuddy.model.UserEntity;
import com.openclassrooms.paymybuddy.security.AppUserDetails;
import com.openclassrooms.paymybuddy.security.AppUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class PageControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    void loginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void registerPage_shouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void registerPage_withSuccessParam_shouldShowSuccessMessage() throws Exception {
        mockMvc.perform(get("/register").param("success", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Compte créé correctement.")));
    }


    @Test
    void root_shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void profile_whenNotAuthenticated_shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }

    @Test
    void profile_whenAuthenticated_shouldShowProfileViewWithUser() throws Exception {
        mockMvc.perform(get("/profile").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("monica")));
    }

    @Test
    void connectionsAddPage_withSuccessFlash_shouldShowSuccessMessage() throws Exception {
        mockMvc.perform(get("/connections/add")
                        .with(user(principal))
                        .flashAttr("success", "Relation ajoutée avec succès."))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Relation ajoutée avec succès.")));
    }

    @Test
    void connectionsAddPage_withErrorFlash_shouldShowErrorMessage() throws Exception {
        mockMvc.perform(get("/connections/add")
                        .with(user(principal))
                        .flashAttr("error", "Aucun utilisateur trouvé avec cet email"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Aucun utilisateur trouvé avec cet email")));
    }
}

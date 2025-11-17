package com.openclassrooms.paymybuddy.controller.web;

import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void register_withValidForm_shouldCallServiceAndRedirectWithSuccess() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "monica.geller@test.com")
                        .param("username", "monicageller")
                        .param("password", "Password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success=1"));

        verify(userService).register("monica.geller@test.com", "monicageller", "Password123");
    }

    @Test
    void register_withInvalidForm_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "wrongemail.com")
                        .param("username", "")
                        .param("password", "xx"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.email").value("must be a well-formed email address"))
                .andExpect(jsonPath("$.errors.username").value("must not be blank"))
                .andExpect(jsonPath("$.errors.password").value("size must be between 8 and 100"));
    }
}

package com.openclassrooms.paymybuddy.controller.api;

import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConnectionRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConnectionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void addConnection_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/connections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ownerId": 1,"friendId": 2}
                                """))
                .andExpect(status().isCreated());

        verify(userService).addConnection(1, 2);
    }

    @Test
    void addConnection_whenInvalid_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/connections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"ownerId": null,"friendId": 2}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }
}

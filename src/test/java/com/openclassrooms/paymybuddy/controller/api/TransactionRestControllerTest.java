package com.openclassrooms.paymybuddy.controller.api;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createTransaction_withValidData_shouldReturnCreated() throws Exception {
        when(transactionService.createTransaction(
                eq(1),
                eq(2),
                eq("test"),
                eq(new BigDecimal("10.00"))
        )).thenReturn(
                TransactionEntity.builder()
                        .id(99)
                        .description("test")
                        .amount(new BigDecimal("0.05"))
                        .build()
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"senderId": 1,"receiverId": 2,"description": "test", "amount": 10.00}
                                """))
                .andExpect(status().isCreated());
    }
}

package com.example.controller;

import com.example.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testMakePayment() throws Exception {
        when(paymentService.makePayment(1L, "credit")).thenReturn("Payment successful");

        mockMvc.perform(post("/payments/pay/1")
                .param("method", "credit"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful"));
    }

    @Test
    void testRefundPayment() throws Exception {
        when(paymentService.refundPayment(1L, 100.0)).thenReturn("Refund processed");

        mockMvc.perform(post("/payments/refund/1")
                .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Refund processed"));
    }
}
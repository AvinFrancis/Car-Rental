package com.example.service;

import com.example.model.Booking;
import com.example.model.Payment;
import com.example.repository.BookingRepository;
import com.example.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makePayment_Successful() {
        Booking booking = new Booking();
        booking.setPaid(false);
        booking.setTotalAmount(100.0);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        String result = paymentService.makePayment(1L, "CREDIT_CARD");

        assertEquals("Payment successful", result);
        verify(bookingRepository).save(booking);
    }

    @Test
    void makePayment_AlreadyPaid_ReturnsMessage() {
        Booking booking = new Booking();
        booking.setPaid(true);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        String result = paymentService.makePayment(1L, "CREDIT_CARD");

        assertEquals("Booking already paid", result);
    }
}
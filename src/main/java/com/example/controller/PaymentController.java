package com.example.controller;

import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint to make a payment for a booking
    @PostMapping("/pay/{bookingId}")
    public String pay(@PathVariable Long bookingId,
                      @RequestParam String method) {
        return paymentService.makePayment(bookingId, method);
    }

    // Endpoint to refund a payment
    @PostMapping("/refund/{paymentId}")
    public String refund(@PathVariable Long paymentId,
                         @RequestParam double amount) {
        return paymentService.refundPayment(paymentId, amount);
    }
}
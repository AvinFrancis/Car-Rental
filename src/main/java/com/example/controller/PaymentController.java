package com.example.controller;

import com.example.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{bookingId}")
    public String pay(@PathVariable Long bookingId, @RequestParam String method) {
        logger.info("Processing payment for booking ID: {} with method: {}", bookingId, method);
        try {
            String result = paymentService.makePayment(bookingId, method);
            logger.debug("Payment processed successfully for booking ID: {}", bookingId);
            return result;
        } catch (Exception e) {
            logger.error("Payment processing failed for booking ID: {}: {}", bookingId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/refund/{paymentId}")
    public String refund(@PathVariable Long paymentId, @RequestParam double amount) {
        logger.info("Processing refund of {} for payment ID: {}", amount, paymentId);
        try {
            String result = paymentService.refundPayment(paymentId, amount);
            logger.debug("Refund processed successfully for payment ID: {}", paymentId);
            return result;
        } catch (Exception e) {
            logger.error("Refund processing failed for payment ID: {}: {}", paymentId, e.getMessage(), e);
            throw e;
        }
    }
}
package com.example.service;

import com.example.model.Booking;
import com.example.model.Payment;
import com.example.repository.BookingRepository;
import com.example.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.exception.PaymentFailedException;

@Service
public class PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String makePayment(Long bookingId, String paymentMethod) {
        logger.info("Processing payment for booking ID: {} with method: {}", bookingId, paymentMethod);
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            logger.warn("Booking not found with ID: {}", bookingId);
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        Booking booking = optionalBooking.get();
        logger.debug("Found booking: {}", booking.getId());

        if (booking.isPaid()) {
            logger.warn("Booking already paid for ID: {}", bookingId);
            throw new PaymentFailedException("Booking already paid");
        }

        try {
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod(paymentMethod);
            payment.setSuccess(true); // Simulate successful payment
            payment.setPaymentTime(LocalDateTime.now());
            logger.debug("Created payment object: {}", payment);

            paymentRepository.save(payment);
            booking.setPaid(true);
            bookingRepository.save(booking);
            logger.debug("Payment successful for booking ID: {}", bookingId);
            return "Payment successful";
        } catch (Exception e) {
            logger.error("Failed to process payment for booking ID: {}: {}", bookingId, e.getMessage(), e);
            throw new PaymentFailedException("Payment processing failed: " + e.getMessage());
        }
    }

    public String refundPayment(Long paymentId, double refundAmount) {
        logger.info("Processing refund of {} for payment ID: {}", refundAmount, paymentId);
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            logger.warn("Payment not found with ID: {}", paymentId);
            throw new RuntimeException("Payment not found with ID: " + paymentId);
        }

        Payment payment = optionalPayment.get();
        logger.debug("Found payment: {}", paymentId);

        if (!payment.isSuccess()) {
            logger.warn("Cannot refund unsuccessful payment with ID: {}", paymentId);
            throw new PaymentFailedException("Cannot refund an unsuccessful payment");
        }

        if (payment.isRefunded()) {
            logger.warn("Payment already refunded with ID: {}", paymentId);
            throw new PaymentFailedException("Payment already refunded");
        }

        if (refundAmount > payment.getAmount()) {
            logger.warn("Refund amount {} exceeds original payment amount {} for ID: {}", refundAmount, payment.getAmount(), paymentId);
            throw new PaymentFailedException("Refund amount exceeds original payment");
        }

        try {
            payment.setRefunded(true);
            payment.setRefundAmount(refundAmount);
            payment.setRefundTime(LocalDateTime.now());
            Booking booking = payment.getBooking();
            booking.setPaid(false);

            paymentRepository.save(payment);
            bookingRepository.save(booking);
            logger.debug("Refund processed successfully for payment ID: {}", paymentId);
            return "Refund processed";
        } catch (Exception e) {
            logger.error("Failed to process refund for payment ID: {}: {}", paymentId, e.getMessage(), e);
            throw new PaymentFailedException("Refund processing failed: " + e.getMessage());
        }
    }
}
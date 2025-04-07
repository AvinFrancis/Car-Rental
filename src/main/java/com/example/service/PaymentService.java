package com.example.service;

import com.example.model.Booking;
import com.example.model.Payment;
import com.example.repository.BookingRepository;
import com.example.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

   
    //  Payment Processing
    
    public String makePayment(Long bookingId, String paymentMethod) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            return "Booking not found";
        }

        Booking booking = optionalBooking.get();

        if (booking.isPaid()) {
            return "Booking already paid";
        }

        // Create payment object
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setSuccess(true); // simulate successful payment
        payment.setPaymentTime(LocalDateTime.now());

        // Save payment
        paymentRepository.save(payment);

        // Update booking status
        booking.setPaid(true);
        bookingRepository.save(booking);

        return "Payment successful";
    }

    
    // Simulate Refund 
   
    public String refundPayment(Long paymentId, double refundAmount) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            return "Payment not found";
        }

        Payment payment = optionalPayment.get();

        if (!payment.isSuccess()) {
            return "Cannot refund an unsuccessful payment";
        }

        if (payment.isRefunded()) {
            return "Already refunded";
        }

        if (refundAmount > payment.getAmount()) {
            return "Refund amount exceeds original payment";
        }

        payment.setRefunded(true);
        payment.setRefundAmount(refundAmount);
        payment.setRefundTime(LocalDateTime.now());

        // Update booking paid status
        Booking booking = payment.getBooking();
        booking.setPaid(false);

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        return "Refund processed";
    }
}

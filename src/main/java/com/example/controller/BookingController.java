package com.example.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.model.Booking;
import com.example.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Booking createBooking(@RequestBody Booking booking) {
        logger.info("Creating new booking: {}", booking);
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            logger.debug("Successfully created booking with ID: {}", createdBooking.getId());
            return createdBooking;
        } catch (Exception e) {
            logger.error("Failed to create booking: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        logger.debug("Retrieved {} bookings", bookings.size());
        return bookings;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public Booking getBookingById(@PathVariable Long id) {
        logger.info("Fetching booking with ID: {}", id);
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            logger.debug("Successfully retrieved booking: {}", booking);
        } else {
            logger.warn("Booking not found with ID: {}", id);
        }
        return booking;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteBooking(@PathVariable Long id) {
        logger.info("Deleting booking with ID: {}", id);
        bookingService.deleteBooking(id);
        logger.debug("Successfully deleted booking with ID: {}", id);
    }
}
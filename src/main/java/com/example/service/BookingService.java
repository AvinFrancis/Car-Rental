package com.example.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Booking;
import com.example.model.Car;
import com.example.repository.BookingRepository;
import com.example.repository.CarRepository;
import com.example.repository.UserRepository;
import com.example.repository.LocationRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Booking createBooking(Booking booking) {
        // Validate car
        Car car = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + booking.getCar().getId()));

        // Validate user
        userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + booking.getUser().getId()));

        // Validate locations
        locationRepository.findById(booking.getPickupLocation().getId())
                .orElseThrow(() -> new RuntimeException("Pickup location not found"));

        locationRepository.findById(booking.getDropoffLocation().getId())
                .orElseThrow(() -> new RuntimeException("Dropoff location not found"));

        // Calculate rental duration
        long days = ChronoUnit.DAYS.between(booking.getPickupDate(), booking.getDropoffDate());
        if (days <= 0) {
            throw new IllegalArgumentException("Dropoff date must be after pickup date");
        }

        // Calculate total amount
        double totalAmount = car.getPricePerDay() * days;
        booking.setTotalAmount(totalAmount);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }
}

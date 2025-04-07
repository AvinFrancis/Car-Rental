package com.example.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.BookingConflictException;
import com.example.model.Booking;
import com.example.model.Car;
import com.example.repository.BookingRepository;
import com.example.repository.CarRepository;
import com.example.repository.UserRepository;
import com.example.repository.LocationRepository;

@Service
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Booking createBooking(Booking booking) {
        logger.info("Creating new booking: {}", booking);
        try {
            Car car = carRepository.findById(booking.getCar().getId())
                    .orElseThrow(() -> {
                        logger.warn("Car not found with ID: {}", booking.getCar().getId());
                        return new RuntimeException("Car not found with ID: " + booking.getCar().getId());
                    });
            logger.debug("Validated car with ID: {}", car.getId());

            // Check car availability
            if (!car.isAvailable()) {
                logger.warn("Car with ID: {} is not available for booking", car.getId());
                throw new BookingConflictException("Car is not available for booking");
            }

            userRepository.findById(booking.getUser().getId())
                    .orElseThrow(() -> {
                        logger.warn("User not found with ID: {}", booking.getUser().getId());
                        return new RuntimeException("User not found with ID: " + booking.getUser().getId());
                    });
            logger.debug("Validated user with ID: {}", booking.getUser().getId());

            locationRepository.findById(booking.getPickupLocation().getId())
                    .orElseThrow(() -> {
                        logger.warn("Pickup location not found with ID: {}", booking.getPickupLocation().getId());
                        return new RuntimeException("Pickup location not found");
                    });
            locationRepository.findById(booking.getDropoffLocation().getId())
                    .orElseThrow(() -> {
                        logger.warn("Dropoff location not found with ID: {}", booking.getDropoffLocation().getId());
                        return new RuntimeException("Dropoff location not found");
                    });
            logger.debug("Validated pickup and dropoff locations");

            long days = ChronoUnit.DAYS.between(booking.getPickupDate(), booking.getDropoffDate());
            if (days <= 0) {
                logger.error("Invalid booking dates: Dropoff date must be after pickup date");
                throw new IllegalArgumentException("Dropoff date must be after pickup date");
            }
            logger.debug("Calculated rental duration: {} days", days);

            double totalAmount = car.getPricePerDay() * days;
            booking.setTotalAmount(totalAmount);
            logger.debug("Calculated total amount: {}", totalAmount);

            // Update car availability
            car.setAvailable(false);
            carRepository.save(car);

            Booking savedBooking = bookingRepository.save(booking);
            logger.debug("Successfully created booking with ID: {}", savedBooking.getId());
            return savedBooking;
        } catch (Exception e) {
            logger.error("Failed to create booking: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Other methods remain unchanged for this example
    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        logger.debug("Retrieved {} bookings", bookings.size());
        return bookings;
    }

    public Booking getBookingById(Long id) {
        logger.info("Fetching booking with ID: {}", id);
        return bookingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Booking not found with ID: {}", id);
                    return new RuntimeException("Booking not found with ID: " + id);
                });
    }

    public void deleteBooking(Long id) {
        logger.info("Deleting booking with ID: {}", id);
        if (!bookingRepository.existsById(id)) {
            logger.warn("Booking not found with ID: {}", id);
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
        logger.debug("Successfully deleted booking with ID: {}", id);
    }
}
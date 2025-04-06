package com.example.service;

import com.example.model.Booking;
import com.example.model.Car;
import com.example.repository.BookingRepository;
import com.example.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    // Create a new booking
    public Booking createBooking(Booking booking) {

        Long carId = booking.getCar().getId(); // Get car ID from booking request

        // Check if the car exists
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            throw new RuntimeException("Car with ID " + carId + " not found.");
        }

        // Check if the car is already booked for the given date range
        if (!isCarAvailableForBooking(carId, booking.getPickupDate(), booking.getDropoffDate())) {
            throw new RuntimeException("Car is already booked for the selected date range.");
        }

        //Calculate total amount based on pricePerDay and number of days
        Car car = optionalCar.get();
        long numberOfDays = booking.getDropoffDate().toEpochDay() - booking.getPickupDate().toEpochDay();
        if (numberOfDays <= 0) {
            throw new RuntimeException("Dropoff date must be after pickup date.");
        }

        double totalAmount = numberOfDays * car.getPricePerDay();
        booking.setTotalAmount(totalAmount);

        // Save and return the booking
        return bookingRepository.save(booking);
    }

    // Helper method to check if the car is available in the selected date range
    private boolean isCarAvailableForBooking(Long carId, LocalDate pickupDate, LocalDate dropoffDate) {
        List<Booking> existingBookings = bookingRepository.findAll();

        for (Booking b : existingBookings) {
            if (b.getCar().getId().equals(carId)) {
                boolean overlaps = !(dropoffDate.isBefore(b.getPickupDate()) || pickupDate.isAfter(b.getDropoffDate()));
                if (overlaps) {
                    return false;
                }
            }
        }

        return true;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    // Update an existing booking
    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existing = getBookingById(id);

        // You can extend this to re-check availability if car or dates change
        existing.setPickupDate(updatedBooking.getPickupDate());
        existing.setDropoffDate(updatedBooking.getDropoffDate());
        existing.setPickupLocation(updatedBooking.getPickupLocation());
        existing.setDropoffLocation(updatedBooking.getDropoffLocation());
        existing.setPaid(updatedBooking.isPaid());

        // Optional: recalculate amount
        long days = existing.getDropoffDate().toEpochDay() - existing.getPickupDate().toEpochDay();
        double newAmount = days * existing.getCar().getPricePerDay();
        existing.setTotalAmount(newAmount);

        return bookingRepository.save(existing);
    }

    // Delete a booking
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }
}

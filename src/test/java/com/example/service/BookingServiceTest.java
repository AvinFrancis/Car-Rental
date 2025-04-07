package com.example.service;

import com.example.model.Booking;
import com.example.model.Car;
import com.example.model.User;
import com.example.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_Successful() {
        Booking booking = new Booking();
        
        // Set up Car with ID
        Car car = new Car();
        car.setId(1L); // Set a non-null ID
        car.setPricePerDay(50.0);
        booking.setCar(car);
        
        // Set up User with ID
        User user = new User();
        user.setId(1L);
        booking.setUser(user);
        
        // Set up Locations with IDs
        com.example.model.Location pickupLocation = new com.example.model.Location();
        pickupLocation.setId(1L);
        com.example.model.Location dropoffLocation = new com.example.model.Location();
        dropoffLocation.setId(2L);
        booking.setPickupLocation(pickupLocation);
        booking.setDropoffLocation(dropoffLocation);
        
        // Set valid dates
        booking.setPickupDate(LocalDate.now());
        booking.setDropoffDate(LocalDate.now().plusDays(2));
        
        // Mock repository responses with specific IDs
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(dropoffLocation));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);
        
        assertNotNull(result);
        assertEquals(100.0, result.getTotalAmount());
        verify(bookingRepository).save(booking);
    }

    @Test
    void createBooking_InvalidDates_ThrowsException() {
        Booking booking = new Booking();
        
        // Set up Car
        Car car = new Car();
        car.setId(1L);
        booking.setCar(car);
        
        // Set up User with an ID
        User user = new User();
        user.setId(1L);
        booking.setUser(user);
        
        // Set up Locations
        com.example.model.Location pickupLocation = new com.example.model.Location();
        pickupLocation.setId(1L);
        com.example.model.Location dropoffLocation = new com.example.model.Location();
        dropoffLocation.setId(2L);
        booking.setPickupLocation(pickupLocation);
        booking.setDropoffLocation(dropoffLocation);
        
        // Set invalid dates
        booking.setPickupDate(LocalDate.now());
        booking.setDropoffDate(LocalDate.now().minusDays(1));

        // Mock repository responses
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(dropoffLocation));

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(booking));
    }

    @Test
    void getAllBookings_Successful() {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(2, result.size());
        verify(bookingRepository).findAll();
    }
}
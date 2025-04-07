package com.example.service;

import com.example.model.Car;
import com.example.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCars_Successful() {
        List<Car> cars = Arrays.asList(new Car(), new Car());
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertEquals(2, result.size());
        verify(carRepository).findAll();
    }

    @Test
    void addCar_Successful() {
        Car car = new Car();
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car result = carService.addCar(car);

        assertNotNull(result);
        verify(carRepository).save(car);
    }

    @Test
    void updateCar_Successful() {
        Car existingCar = new Car();
        Car updatedCar = new Car();
        updatedCar.setModel("New Model");
        
        when(carRepository.findById(1L)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenReturn(existingCar);

        Car result = carService.updateCar(1L, updatedCar);

        assertEquals("New Model", result.getModel());
        verify(carRepository).save(existingCar);
    }
}
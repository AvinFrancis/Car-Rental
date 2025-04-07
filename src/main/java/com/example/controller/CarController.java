package com.example.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.model.Car;
import com.example.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {
    
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    
    @Autowired
    private CarService carService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Car addCar(@RequestBody Car car) {
        logger.info("Attempting to add new car: {}", car);
        try {
            Car savedCar = carService.addCar(car);
            logger.debug("Successfully added car with ID: {}", savedCar.getId());
            return savedCar;
        } catch (Exception e) {
            logger.error("Failed to add car: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public List<Car> getAllCars(){
        logger.info("Fetching all cars");
        List<Car> cars = carService.getAllCars();
        logger.debug("Retrieved {} cars", cars.size());
        return cars;
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Car getCarById(@PathVariable Long id){
        logger.info("Fetching car with ID: {}", id);
        Car car = carService.getCarById(id);
        if (car != null) {
            logger.debug("Successfully retrieved car: {}", car);
        } else {
            logger.warn("Car not found with ID: {}", id);
        }
        return car;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        logger.info("Updating car with ID: {} with details: {}", id, car);
        Car updatedCar = carService.updateCar(id, car);
        logger.debug("Successfully updated car with ID: {}", id);
        return updatedCar;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteCar(@PathVariable Long id){
        logger.info("Deleting car with ID: {}", id);
        carService.deleteCar(id);
        logger.debug("Successfully deleted car with ID: {}", id);
    }
}
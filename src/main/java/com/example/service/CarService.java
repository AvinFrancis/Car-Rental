package com.example.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Car;
import com.example.repository.CarRepository;

@Service
public class CarService {
    
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars(){
        logger.info("Fetching all cars");
        List<Car> cars = carRepository.findAll();
        logger.debug("Retrieved {} cars", cars.size());
        return cars;
    }

    public Car getCarById(Long id) {
        logger.info("Fetching car with ID: {}", id);
        return carRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Car not found with ID: {}", id);
                    return new RuntimeException("Car not found with ID: " + id);
                });
    }

    public Car addCar(Car car){
        logger.info("Adding new car: {}", car);
        try {
            Car savedCar = carRepository.save(car);
            logger.debug("Successfully added car with ID: {}", savedCar.getId());
            return savedCar;
        } catch (Exception e) {
            logger.error("Failed to add car: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Car updateCar(Long id, Car updatedCar){
        logger.info("Updating car with ID: {} with details: {}", id, updatedCar);
        return carRepository.findById(id).map(car -> {
            logger.debug("Found car with ID: {}, updating details", id);
            car.setModel(updatedCar.getModel());
            car.setBrand(updatedCar.getBrand());
            car.setLicensePlate(updatedCar.getLicensePlate());
            car.setPricePerDay(updatedCar.getPricePerDay());
            car.setAvailable(updatedCar.isAvailable());
            Car savedCar = carRepository.save(car);
            logger.debug("Successfully updated car with ID: {}", id);
            return savedCar;
        }).orElseThrow(() -> {
            logger.warn("Car not found for update with ID: {}", id);
            return new RuntimeException("Car not found");
        });
    }

    public void deleteCar(Long id){
        logger.info("Deleting car with ID: {}", id);
        if(!carRepository.existsById(id)){
            logger.warn("Car not found with ID: {}", id);
            throw new RuntimeException("Car Not Found with id: " + id);
        }
        carRepository.deleteById(id);
        logger.debug("Successfully deleted car with ID: {}", id);
    }
}
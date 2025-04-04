 package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Car;
import com.example.repository.CarRepository;

@Service
public class CarService {

    @Autowired // Automatically injects the CarRepository bean
    private CarRepository carRepository;

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + id));
    }

    public Car addCar(Car car){
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar){
        return carRepository.findById(id).map(car->{
            car.setModel(updatedCar.getModel());
        car.setBrand(updatedCar.getBrand());
        car.setLicensePlate(updatedCar.getLicensePlate());
        car.setPricePerDay(updatedCar.getPricePerDay());
        car.setAvailable(updatedCar.isAvailable());
        return carRepository.save(car);
    }).orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public void deleteCar(Long id){
        if(!carRepository.existsById(id)){
            throw new RuntimeException("Car Not Found with id: "+id);
        }
        carRepository.deleteById(id);//deletes car
    }
}

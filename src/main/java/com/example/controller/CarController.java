package com.example.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.model.Car;
import com.example.service.CarService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/cars") //base URL for all car related endpoints
public class CarController {

    @Autowired //injects CarService
    private CarService carService;
    
    //post method to add car
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    //get method to get all cars
    @GetMapping
    public List<Car> getAllCars(){
        return carService.getAllCars();
    }
    
    //get by Id
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id){
        return carService.getCarById(id);
    }

    //Update car
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    //Delete a car
    @DeleteMapping("/{id}")
    
    public void deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
    }
}

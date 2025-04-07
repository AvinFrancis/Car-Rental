package com.example.service;

import com.example.model.Location;
import com.example.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    // Inject the repository to interact with the database
    @Autowired
    private LocationRepository locationRepository;

    // Save a new location or update existing one
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    // Get all locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // Get a specific location by ID
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    // Delete a location by ID
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}

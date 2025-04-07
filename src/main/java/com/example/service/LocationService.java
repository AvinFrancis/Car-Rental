package com.example.service;

import com.example.model.Location;
import com.example.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    
    @Autowired
    private LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        logger.info("Saving location: {}", location);
        try {
            Location savedLocation = locationRepository.save(location);
            logger.debug("Successfully saved location with ID: {}", savedLocation.getId());
            return savedLocation;
        } catch (Exception e) {
            logger.error("Failed to save location: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Location> getAllLocations() {
        logger.info("Fetching all locations");
        List<Location> locations = locationRepository.findAll();
        logger.debug("Retrieved {} locations", locations.size());
        return locations;
    }

    public Optional<Location> getLocationById(Long id) {
        logger.info("Fetching location with ID: {}", id);
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            logger.debug("Found location: {}", location.get());
        } else {
            logger.warn("Location not found with ID: {}", id);
        }
        return location;
    }

    public void deleteLocation(Long id) {
        logger.info("Deleting location with ID: {}", id);
        try {
            locationRepository.deleteById(id);
            logger.debug("Successfully deleted location with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete location with ID: {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
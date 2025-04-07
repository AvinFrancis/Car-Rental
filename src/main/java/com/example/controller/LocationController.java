package com.example.controller;

import com.example.model.Location;
import com.example.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationController {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    
    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        logger.info("Creating new location: {}", location);
        try {
            Location savedLocation = locationService.saveLocation(location);
            logger.debug("Successfully created location with ID: {}", savedLocation.getId());
            return ResponseEntity.ok(savedLocation);
        } catch (Exception e) {
            logger.error("Failed to create location: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        logger.info("Fetching all locations");
        List<Location> locations = locationService.getAllLocations();
        logger.debug("Retrieved {} locations", locations.size());
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        logger.info("Fetching location with ID: {}", id);
        Optional<Location> location = locationService.getLocationById(id);
        if (location.isPresent()) {
            logger.debug("Successfully retrieved location: {}", location.get());
            return ResponseEntity.ok(location.get());
        } else {
            logger.warn("Location not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location locationDetails) {
        logger.info("Updating location with ID: {} with details: {}", id, locationDetails);
        Optional<Location> optionalLocation = locationService.getLocationById(id);

        if (optionalLocation.isPresent()) {
            Location existing = optionalLocation.get();
            existing.setName(locationDetails.getName());
            existing.setAddress(locationDetails.getAddress());
            existing.setCity(locationDetails.getCity());
            existing.setCountry(locationDetails.getCountry());
            Location updatedLocation = locationService.saveLocation(existing);
            logger.debug("Successfully updated location with ID: {}", id);
            return ResponseEntity.ok(updatedLocation);
        } else {
            logger.warn("Location not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        logger.info("Deleting location with ID: {}", id);
        Optional<Location> optionalLocation = locationService.getLocationById(id);
        if (optionalLocation.isPresent()) {
            locationService.deleteLocation(id);
            logger.debug("Successfully deleted location with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Location not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
package com.example.controller;

import com.example.model.Location;
import com.example.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // Create a new location
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location savedLocation = locationService.saveLocation(location);
        return ResponseEntity.ok(savedLocation);
    }

    // Get all locations
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    //  Get a location by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    //  Update an existing location
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location locationDetails) {
        Optional<Location> optionalLocation = locationService.getLocationById(id);

        if (optionalLocation.isPresent()) {
            Location existing = optionalLocation.get();
            existing.setName(locationDetails.getName());
            existing.setAddress(locationDetails.getAddress());
            existing.setCity(locationDetails.getCity());
            existing.setCountry(locationDetails.getCountry());
            Location updatedLocation = locationService.saveLocation(existing);
            return ResponseEntity.ok(updatedLocation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a location
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        Optional<Location> optionalLocation = locationService.getLocationById(id);
        if (optionalLocation.isPresent()) {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

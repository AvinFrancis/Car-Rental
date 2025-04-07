package com.example.service;

import com.example.model.Location;
import com.example.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLocation_Successful() {
        Location location = new Location();
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        Location result = locationService.saveLocation(location);

        assertNotNull(result);
        verify(locationRepository).save(location);
    }

    @Test
    void getAllLocations_Successful() {
        List<Location> locations = Arrays.asList(new Location(), new Location());
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        verify(locationRepository).findAll();
    }
}
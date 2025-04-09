package org.sportstogo.backend.Controller;


import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {this.locationService = locationService;}
    @GetMapping
    public List<Location> getLocations() {return locationService.getLocations();}

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody Location location) {
        this.locationService.addNewLocation(location);
        return ResponseEntity.ok().body("Location registered successfully");
    }

    @DeleteMapping(path="{location_id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("location_id") Long id) {
        locationService.deleteById(id);
        return ResponseEntity.ok().body("Location deleted successfully");
    }

    @PutMapping(path="{location_id}")
    public ResponseEntity<String> updateLocation(@PathVariable("location_id") Long id,
                                                 @RequestParam String name) {
        locationService.updateLocation(id, name);
        return ResponseEntity.ok().body("Location updated successfully");
    }
}

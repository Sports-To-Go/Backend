package org.sportstogo.backend.Controller;


import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Models.Sport;
import org.sportstogo.backend.Service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing Location entities
 * Provides endpoints for CRUD operations on locations
 */
@RestController
@RequestMapping(path="locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {this.locationService = locationService;}

    /**
     * @return a list of all locations
     */
    @GetMapping
    public List<Location> getLocations() {return locationService.getLocations();}

    /**
     * Retrieves all locations matching a series of filters
     * @return a list of all locations
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Location>> getFiltered(@RequestParam(required = false) Sport sport,
                                                      @RequestParam(required = false) LocalTime start,
                                                      @RequestParam(required = false) LocalTime end,
                                                      @RequestParam(required = false) Double price) {
        Optional<List<Location>> locations=locationService.getFiltered(sport, start, end, price);
        if(locations.isPresent()) {
            return ResponseEntity.ok(locations.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Adds a new location tot the database
     * @param location the location object from the request body
     * @return appropriate HTTP status code
     */
    @PostMapping

    public ResponseEntity<String> addLocation(@RequestBody Location location) {
        return locationService.addNewLocation(location);
    }

    /**
     * Deletes a location by its ID
     * @param id the ID of the location to delete
     * @return appropriate HTTP status code
     */
    @DeleteMapping(path="{location_id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("location_id") Long id) {
        return locationService.deleteById(id);
    }

    /**
     * Updates the name of an existing location
     * @param id the ID of the location whose name is to be changed
     * @param name the new name of the location
     * @return appropriate HTTP code
     */
    @PutMapping(path="{location_id}")
    public ResponseEntity<String> updateLocation(@PathVariable("location_id") Long id,
                                                 @RequestParam String name) {
        return locationService.updateLocation(id, name);
    }
}

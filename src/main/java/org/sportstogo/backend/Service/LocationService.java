package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Models.Sport;
import org.sportstogo.backend.Repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link Location} entities and related operations
 */
@Service
@AllArgsConstructor
public class LocationService {
    private LocationRepository locationRepository;

    /**
     * Retrieves all the locations from the database
     * @return a list of all locations
     */
    public List<Location> getLocations() {return locationRepository.findAll();}

    /**
     * Retrieves all locations matching a filter
     * @return a list of all verified locations matching the filter
     */
    public List<Location> getFiltered(Sport sport, LocalTime openingTime,
                                                LocalTime closingTime, Double hourlyRate) {
        List<Location> locations = locationRepository.findAll();
        if(locations.isEmpty()) {
            return locations;
        }
        List<Location> filteredLocations = new ArrayList<>(locations);
        for(Location location : locations) {
            if(sport!=null && !location.getSport().equals(sport)) {
                filteredLocations.remove(location);
            }
            else if(openingTime!=null && location.getClosingTime().isBefore(openingTime)) {
                filteredLocations.remove(location);
            }
            else if(closingTime!=null && location.getOpeningTime().isAfter(closingTime)) {
                filteredLocations.remove(location);
            }
            else if(hourlyRate!=null && location.getHourlyRate()>=hourlyRate) {
                filteredLocations.remove(location);
            }
        }
        return filteredLocations;
    }
    /**
     * Adds a new location to the database
     * @param location the location object from the request body
     * @return HTTP CREATED if successful, HTTP CONFLICT otherwise
     */
    public ResponseEntity<String> addNewLocation(Location location) {
        if(this.locationRepository.findByName(location.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The location already exists");
        }
        location.setCreatedAt(LocalDate.now());
        location.setVerified(false);
        this.locationRepository.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body("Location successfully added");
    }

    /**
     * Deletes a location by its ID
     * @param id the ID of the location to be removed
     * @return HTTP OK if successful, HTTP CONFLICT otherwise
     */
    public ResponseEntity<String> deleteById(Long id) {
        if(locationRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Location not found");
        }
        locationRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Location successfully deleted");
    }

    /**
     * Updates the name of an existing location
     * @param id the id of the location whose name is to be changed
     * @param name the new name of the location
     * @return HTTP OK if successful, HTTP CONFLICT otherwise
     */
    @Transactional
    public ResponseEntity<String> updateLocation(Long id, String name) {
        Optional<Location> locationOp = locationRepository.findById(id);
        if (locationOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Location not found");
        }
        Location location = locationOp.get();
        location.setName(name);
        return ResponseEntity.status(HttpStatus.OK).body("Location successfully updated");
    }

}

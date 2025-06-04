package org.sportstogo.backend.Controller;

import lombok.RequiredArgsConstructor;
import org.sportstogo.backend.DTOs.LocationDTO;
import org.sportstogo.backend.Enums.Sport;
import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(path = "locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationDTO> getLocations() {
        return locationService.getAllLocationDTOs();
    }


    @GetMapping("/filter")
    public List<LocationDTO> getFiltered(
            @RequestParam(required = false) Sport sport,
            @RequestParam(required = false) LocalTime start,
            @RequestParam(required = false) LocalTime end,
            @RequestParam(required = false) String price) {
        return locationService.getFiltered(sport, start, end, price).stream().map(Location::mapToDTO).toList();
    }
    @GetMapping("/{uid}")
    public ResponseEntity<?> getAllByUserId(@PathVariable String uid) {
        return ResponseEntity.ok().body(locationService.getLocationDTOsByUserId(uid));
    }

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody Location location) {
        return locationService.addNewLocation(location);
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<LocationDTO> addLocationWithImages(
            @RequestPart("location") Location location,
            @RequestPart("images") List<MultipartFile> images) {
        LocationDTO dto = locationService.addLocationWithImages(location, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @DeleteMapping(path = "{location_id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("location_id") Long id) {
        return locationService.deleteById(id);
    }

    @PutMapping(path = "{location_id}")
    public ResponseEntity<String> updateLocation(@PathVariable("location_id") Long id,
                                                 @RequestParam String name) {
        return locationService.updateLocation(id, name);
    }
}

package org.sportstogo.backend.Service;

import org.sportstogo.backend.DTOs.LocationDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Enums.Sport;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Models.LocationImage;
import org.sportstogo.backend.Repository.LocationImageRepository;
import org.sportstogo.backend.Repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationImageRepository locationImageRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getFiltered(Sport sport, LocalTime openingTime,
                                      LocalTime closingTime, String priceOrder) {
        return locationRepository.findAll().stream()
                .filter(location -> {
                    boolean matchesSport = (sport == null || location.getSport() == sport);
                    LocalTime locClosing = location.getClosingTime().equals(LocalTime.MIDNIGHT)
                            ? LocalTime.of(23, 59)
                            : location.getClosingTime();
                    boolean matchesTime = true;
                    if (openingTime != null)
                        matchesTime &= location.getOpeningTime().compareTo(openingTime) <= 0;
                    if (closingTime != null)
                        matchesTime &= locClosing.compareTo(closingTime) >= 0;
                    return matchesSport && matchesTime;
                })
                .sorted((l1, l2) -> {
                    if ("ascending".equalsIgnoreCase(priceOrder)) {
                        return Double.compare(l1.getHourlyRate(), l2.getHourlyRate());
                    } else if ("descending".equalsIgnoreCase(priceOrder)) {
                        return Double.compare(l2.getHourlyRate(), l1.getHourlyRate());
                    }
                    return 0;
                })
                .toList();
    }

    public ResponseEntity<String> addNewLocation(Location location) {
        if (locationRepository.findByName(location.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The location already exists");
        }
        if (location.getClosingTime().equals(LocalTime.MIDNIGHT)) {
            location.setClosingTime(LocalTime.of(23, 59));
        }
        location.setCreatedAt(LocalDate.now());
        location.setVerified(false);
        locationRepository.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body("Location successfully added");
    }

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

    public String getLocationNameById(Long id) {
        return locationRepository.findById(id)
                .map(Location::getName)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Location> getLocationByUserId(String uID) {
        return locationRepository.findAllByCreatedBy(uID);
    }

    @Transactional
    public LocationDTO addLocationWithImages(Location location, List<MultipartFile> images) {
        if (images.size() > 10) {
            throw new IllegalArgumentException("Maximum 10 images allowed.");
        }

        location.setCreatedAt(LocalDate.now());
        location.setVerified(false);
        Location savedLocation = locationRepository.save(location);

        for (MultipartFile file : images) {
            Image savedImage = imageService.saveImage(file);
            LocationImage locationImage = new LocationImage();
            locationImage.setLocation(savedLocation);
            locationImage.setImage(savedImage);
            locationImageRepository.save(locationImage);
        }

        // Initialize lazy collection before DTO mapping
        savedLocation.getImages().size();

        return Location.mapToDTO(savedLocation);
    }

    @Transactional
    public ResponseEntity<String> deleteById(Long id) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (locationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Location not found");
        }

        Location location = locationOpt.get();

        // Delete associated images and their mappings
        location.getImages().forEach(locationImage -> {
            Image image = locationImage.getImage();

            // Delete the join table entry
            locationImageRepository.delete(locationImage);

            // Delete the actual image (from DB and S3)
            imageService.deleteImageEntity(image);
        });

        // Finally delete the location
        locationRepository.delete(location);

        return ResponseEntity.ok("Location successfully deleted");
    }




    @Transactional(readOnly = true)
    public List<LocationDTO> getAllLocationDTOs() {
        List<Location> locations = locationRepository.findAll();

        // Force loading of lazy collections before mapping
        locations.forEach(loc -> loc.getImages().size());

        return locations.stream()
                .map(Location::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LocationDTO> getLocationDTOsByUserId(String uid) {
        List<Location> locations = locationRepository.findAllByCreatedBy(uid);

        locations.forEach(loc -> loc.getImages().size());

        return locations.stream()
                .map(Location::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.getImages().size(); // <-- force loading lazy collection
                    return location;
                })
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }

}

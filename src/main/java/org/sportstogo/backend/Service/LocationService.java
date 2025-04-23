package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Location;
import org.sportstogo.backend.Repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {
    private LocationRepository locationRepository;
    public List<Location> getLocations() {return locationRepository.findAll();}

    public void addNewLocation(Location location) {
        if(this.locationRepository.findByName(location.getName()).isPresent()) {
            throw new IllegalArgumentException("Location already exists");
        } else if(this.locationRepository.findByName(location.getName()).isPresent()) {
            throw new IllegalArgumentException("Location already exists");
        }
        location.setCreatedAt(LocalDate.now());
        location.setVerified(false);
        this.locationRepository.save(location);
    }

    public void deleteById(Long id) {locationRepository.deleteById(id);}

    @Transactional
    public void updateLocation(Long id, String name) {
        Optional<Location> locationOp = locationRepository.findById(id);
        if (locationOp.isEmpty()) {
            throw new IllegalArgumentException("Location does not exist");
        }
        Location location = locationOp.get();
        location.setName(name);
    }

}

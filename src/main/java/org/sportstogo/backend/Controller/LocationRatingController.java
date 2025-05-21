package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.LocationRating;
import org.sportstogo.backend.Service.LocationRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST Controller for managing LocationRating entities
 * Provides endpoints for CRUD operations on location ratings
 */
@RestController
@RequestMapping(path = "/location-ratings")
public class LocationRatingController {
    private final LocationRatingService locationRatingService;

    public LocationRatingController(LocationRatingService locationRatingService) {
        this.locationRatingService = locationRatingService;
    }
    /**
     * @return a list of all location ratings
     */
    @GetMapping
    public List<LocationRating> getAllRatings() {
        return locationRatingService.getAllRatings();
    }

    @PostMapping
    public ResponseEntity<String> addNewRating(@RequestBody LocationRating rating) {
        return locationRatingService.addNewRating(rating);
    }

    @DeleteMapping(path = "{rating_id}")
    public ResponseEntity<String> deleteRating(@PathVariable("rating_id") Long id) {
        return locationRatingService.deleteById(id);
    }

    @PutMapping(path = "{rating_id}")
    public ResponseEntity<String> updateRating(
            @PathVariable("rating_id") Long id,
            @RequestParam(required = false) Double score,
            @RequestParam(required = false) String comment) {
        return locationRatingService.updateRating(id, score, comment);
    }
}

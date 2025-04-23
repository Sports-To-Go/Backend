package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.LocationRating;
import org.sportstogo.backend.Service.LocationRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/location-ratings")
public class LocationRatingController {

    private final LocationRatingService locationRatingService;

    public LocationRatingController(LocationRatingService locationRatingService) {
        this.locationRatingService = locationRatingService;
    }

    @GetMapping
    public List<LocationRating> getAllRatings() {
        return locationRatingService.getAllRatings();
    }

    @PostMapping
    public ResponseEntity<String> addNewRating(@RequestBody LocationRating rating) {
        locationRatingService.addNewRating(rating);
        return ResponseEntity.ok()
                .body("The rating was successfully added");
    }

    @DeleteMapping(path = "{rating_id}")
    public ResponseEntity<String> deleteRating(@PathVariable("rating_id") Long id) {
        locationRatingService.deleteById(id);
        return ResponseEntity.ok()
                .body("The rating was successfully deleted");
    }

    @PutMapping(path = "{rating_id}")
    public ResponseEntity<String> updateRating(
            @PathVariable("rating_id") Long id,
            @RequestParam(required = false) Double score,
            @RequestParam(required = false) String comment) {

        locationRatingService.updateRating(id, score, comment);
        return ResponseEntity.ok()
                .body("The rating was successfully updated");
    }
}

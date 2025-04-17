package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.LocationRating;
import org.sportstogo.backend.Repository.LocationRatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationRatingService {

    private LocationRatingRepository locationRatingRepository;

    public List<LocationRating> getAllRatings() {
        return locationRatingRepository.findAll();
    }

    public ResponseEntity<String> addNewRating(LocationRating rating) {
        if (rating.getScore() < 0 || rating.getScore() > 5) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Score should be between 0 and 5");
        }
        if (rating.getCreatedAt() == null) {
            rating.setCreatedAt(LocalDate.now());
        }
        locationRatingRepository.save(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body("Rating has been added");
    }

    public ResponseEntity<String> deleteById(Long id) {
        if (!locationRatingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The rating with this id " + id + " does not exist");
        }
        locationRatingRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Rating has been deleted");
    }

    @Transactional
    public ResponseEntity<String> updateRating(Long id, Double score, String comment) {
        Optional<LocationRating> optionalRating = locationRatingRepository.findById(id);
        if (optionalRating.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The rating is not found");
        }

        LocationRating rating = optionalRating.get();

        if (score != null) {
            if (score < 0 || score > 5) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The score should be between 0 and 5");
            }
            rating.setScore(score);
        }

        if (comment != null && !comment.isBlank()) {
            rating.setComment(comment);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Rating has been updated");
    }
}

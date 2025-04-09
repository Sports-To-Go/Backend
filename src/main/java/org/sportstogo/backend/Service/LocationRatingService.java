package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.LocationRating;
import org.sportstogo.backend.Repository.LocationRatingRepository;
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

    public void addNewRating(LocationRating rating) {
        if (rating.getScore() < 0 || rating.getScore() > 5) {
            throw new IllegalStateException("Score should be between 0 and 5");
        }
        if (rating.getCreatedAt() == null) {
            rating.setCreatedAt(LocalDate.now());
        }
        locationRatingRepository.save(rating);
    }

    public void deleteById(Long id) {
        if (!locationRatingRepository.existsById(id)) {
            throw new IllegalStateException("The rating with this id " + id + " does not exist");
        }
        locationRatingRepository.deleteById(id);
    }

    @Transactional
    public void updateRating(Long id, Double score, String comment) {
        Optional<LocationRating> optionalRating = locationRatingRepository.findById(id);
        if (optionalRating.isEmpty()) {
            throw new IllegalStateException("The rating is not found");
        }

        LocationRating rating = optionalRating.get();

        if (score != null) {
            if (score < 0 || score > 5) {
                throw new IllegalStateException("The score should be between 0 and 5");
            }
            rating.setScore(score);
        }

        if (comment != null && !comment.isBlank()) {
            rating.setComment(comment);
        }
    }
}

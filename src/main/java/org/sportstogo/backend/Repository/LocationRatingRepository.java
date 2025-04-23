package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.LocationRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRatingRepository extends JpaRepository<LocationRating, Long> {
}

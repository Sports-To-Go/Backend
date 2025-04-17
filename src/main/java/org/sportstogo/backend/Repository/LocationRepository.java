package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Location} entities.
 * Extends JpaRepository to provide CRUD operations and pagination.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    @Query("Select l from Location  l where l.name=?1")
    Optional<Location> findByName(String name);

    @Query("Select l from Location  l where l.createdBy=?1")
    Optional<Location> findByCreatedBy(String createdBy);
}

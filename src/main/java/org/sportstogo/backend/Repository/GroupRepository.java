package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Group} entities.
 * Extends JpaRepository to provide CRUD operations and pagination.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    // You can add custom query methods here later if needed.
}

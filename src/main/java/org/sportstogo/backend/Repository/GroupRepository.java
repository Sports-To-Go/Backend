package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Long> {
}

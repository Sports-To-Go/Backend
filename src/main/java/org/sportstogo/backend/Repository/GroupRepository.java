package org.sportstogo.backend.Repository;

import org.sportstogo.backend.DTOs.GroupPreviewDTO;
import org.sportstogo.backend.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(value = """
        SELECT g.id AS id,
               g.name AS name,
               COALESCE(g.description, 'No description') AS description
        FROM groups g
        LEFT JOIN group_memberships gm ON g.id = gm.group_id
        WHERE g.id NOT IN (
            SELECT group_id
            FROM group_memberships
            WHERE user_id = :uid
        )
        AND g.id NOT IN (
            SELECT group_id
            FROM join_requests
            WHERE user_id = :uid
        )
        GROUP BY g.id, g.name, g.description
   \s""", nativeQuery = true)
    List<GroupPreviewDTO> findGroupRecommendations(@Param("uid") String uid);

}


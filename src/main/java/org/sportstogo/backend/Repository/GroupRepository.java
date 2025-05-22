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
        SELECT g.id AS id, g.name AS name,\s
               CAST(COUNT(DISTINCT gm.user_id) AS INT) AS memberCount,\s
               COALESCE(m.content, 'No messages yet') AS description
        FROM groups g
        INNER JOIN group_memberships gm ON g.id = gm.group_id
        LEFT JOIN (
            SELECT m1.group_id, m1.content
            FROM messages m1
            INNER JOIN (
                SELECT group_id, MAX(time_sent) AS max_time_sent
                FROM messages
                GROUP BY group_id
            ) m2 ON m1.group_id = m2.group_id AND m1.time_sent = m2.max_time_sent
        ) m ON g.id = m.group_id
        WHERE g.id IN (
            SELECT group_id
            FROM group_memberships
            WHERE user_id = :uid
        )
        GROUP BY g.id, g.name, m.content
       \s""", nativeQuery = true)
    List<GroupPreviewDTO> findChatPreviewsByUserId(@Param("uid") String uid);

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


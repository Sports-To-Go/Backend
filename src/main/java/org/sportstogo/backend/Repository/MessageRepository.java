package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.groupID.id = :groupId ORDER BY m.timeSent DESC")
    List<Message> findByGroupIdOrderByTimeSentDesc(@Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM Messages WHERE group_id = :groupId ORDER BY time_sent LIMIT :limit",
            nativeQuery = true)
    List<Message> findRecentByGroupId(@Param("groupId") Long groupId, @Param("limit") int limit);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.groupID.id = :groupId")
    long countByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT MAX(m.ID) FROM Message m WHERE m.groupID.id = :groupId")
    Long findMaxIdByGroupId(@Param("groupId") Long groupId);
}
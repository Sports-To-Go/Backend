package org.sportstogo.backend.Repository;

import jakarta.transaction.Transactional;
import org.sportstogo.backend.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM Messages WHERE group_id = :groupId ORDER BY time_sent LIMIT :limit",
            nativeQuery = true)
    List<Message> findRecentByGroupId(@Param("groupId") Long groupId, @Param("limit") int limit);


    @Query("SELECT MAX(m.ID) FROM Message m WHERE m.groupID.id = :groupId")
    Long findMaxIdByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.groupID.id = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);

    @Query(value = """
    INSERT INTO messages (id, group_id, user_id, content, time_sent)
    VALUES (nextval('message_id_seq'), :groupId, :userId, :content, :timeSent)
    RETURNING id
    """, nativeQuery = true)
    Long insert(@Param("groupId") Long groupId,
                            @Param("userId") String userId,
                            @Param("content") String content,
                            @Param("timeSent") LocalDateTime timeSent);


}
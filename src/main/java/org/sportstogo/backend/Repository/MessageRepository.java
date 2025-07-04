package org.sportstogo.backend.Repository;

import jakarta.transaction.Transactional;
import org.sportstogo.backend.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.sportstogo.backend.Enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(
            value = """
    (
      SELECT * FROM messages
      WHERE time_sent < :timestamp
        AND group_id = :groupId
      ORDER BY time_sent DESC 
      LIMIT 50
    )
    UNION ALL
    (
      SELECT * FROM messages
      WHERE time_sent = :timestamp
        AND group_id = :groupId
    )
    ORDER BY time_sent ASC
    """,
            nativeQuery = true
    )
    List<Message> findRecentByGroupId(
            @Param("groupId") Long groupId,
            @Param("timestamp") LocalDateTime timestamp
    );

    @Query(value = """
    INSERT INTO messages (id, group_id, user_id, content, time_sent, type, system_event, meta_data)
    VALUES (nextval('message_id_seq'), :groupId, :userId, :content, :timeSent, :type, :system_event, :meta_data)
    RETURNING id
""", nativeQuery = true)
    Long insert(@Param("groupId") Long groupId,
                               @Param("userId") String userId,
                               @Param("content") String content,
                               @Param("timeSent") LocalDateTime timeSent,
                               @Param("type") Integer type,
                               @Param("system_event") String system_event,
                               @Param("meta_data") String meta_data);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.groupID.id = :groupId AND m.type IN :types")
    void deleteByGroupIdAndTypeIn(@Param("groupId") Long groupId, @Param("types") List<MessageType> types);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.groupID.id = :groupId AND m.type = :type")
    void deleteByGroupIdAndType(@Param("groupId") Long groupId, @Param("type") MessageType type);

    @Query("SELECT m FROM Message m WHERE m.groupID.id = :groupId AND m.type = :type")
    List<Message> findByGroupIdAndType(@Param("groupId") Long groupId, @Param("type") MessageType type);

    @Modifying
    @Query("UPDATE Message m SET m.userID.uid = :newUid WHERE m.userID.uid = :oldUid")
    void updateUserReferences(@Param("oldUid") String oldUid, @Param("newUid") String newUid);

}
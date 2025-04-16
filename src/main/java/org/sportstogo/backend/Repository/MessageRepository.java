package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.idModels.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the Message entity.
 * Provides methods to query messages based on group ID and retrieve messages ordered by their sent time.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, MessageId> {

    /**
     * Finds a limited number of messages from a specific group, ordered by the time they were sent in descending order.
     *
     * @param groupId The ID of the group to fetch messages for.
     * @param n The number of messages to retrieve.
     * @return A list of messages from the specified group, ordered by time sent.
     */
    @Query("select m from Message m where m.groupId = ?1 order by m.timeSent Desc limit ?2")
    List<Message> findByGroupIdNr(Long groupId, int n);

    /**
     * Finds all messages from a specific group, ordered by the time they were sent.
     *
     * @param groupId The ID of the group to fetch messages for.
     * @return A list of all messages from the specified group.
     */
    @Query("select m from Message m where m.groupId = ?1")
    List<Message> findByGroupId(Long groupId);
}

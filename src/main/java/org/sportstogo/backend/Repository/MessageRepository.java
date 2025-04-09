package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.idModels.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, MessageId> {
    @Query("select m from Message m where m.groupId = ?1 order by m.timeSent Desc limit ?2")
    List<Message> findByGroupIdNr(Long groupId, int n);

    @Query("select m from Message m where m.groupId = ?1")
    List<Message> findByGroupId(Long groupId);
}

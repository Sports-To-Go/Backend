package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, GroupMemberID> {

    @Query("SELECT m FROM Message m WHERE m.groupID.id = :groupID ORDER BY m.timeSent DESC LIMIT 1")
    Message findLastMessageByGroupID(@Param("groupID") Long groupID);
}


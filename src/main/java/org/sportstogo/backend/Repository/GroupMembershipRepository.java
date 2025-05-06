package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupMemberID> {
    @Query("SELECT gm.groupID.id FROM GroupMembership gm WHERE gm.userID.uid = :userID")
    List<Long> findGroupIDsByUserID(@Param("userID") String userID);
}

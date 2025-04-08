package org.sportstogo.backend.Repository;


import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepo extends JpaRepository<GroupMembership, GroupMembershipId> {
    @Query("SELECT gm FROM GroupMembership gm WHERE gm.id.groupId = ?1")
    List<GroupMembership> findByGroupId(Long groupId);
}

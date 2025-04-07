package org.sportstogo.backend.GroupMembership;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepo extends JpaRepository<GroupMembership,GroupMembershipId> {
    @Query("SELECT gm FROM GroupMembership gm WHERE gm.id.groupId = ?1")
    List<GroupMembership> findByGroupId(Long groupId);
}

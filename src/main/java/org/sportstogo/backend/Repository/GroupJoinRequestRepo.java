package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.GroupJoinRequest;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupJoinRequestRepo extends JpaRepository<GroupJoinRequest, GroupMembershipId> {
}

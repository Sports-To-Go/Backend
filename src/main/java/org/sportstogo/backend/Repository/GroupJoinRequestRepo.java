package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.GroupJoinRequest;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the GroupJoinRequest entity.
 * Extends JpaRepository for basic persistence operations on GroupJoinRequest.
 */
@Repository
public interface GroupJoinRequestRepo extends JpaRepository<GroupJoinRequest, GroupMembershipId> {

}

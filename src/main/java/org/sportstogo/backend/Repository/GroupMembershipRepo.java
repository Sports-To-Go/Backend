package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing GroupMembership entities.
 * This interface extends JpaRepository to provide CRUD operations for GroupMembership.
 */
@Repository
public interface GroupMembershipRepo extends JpaRepository<GroupMembership, GroupMembershipId> {

    /**
     * Finds all group memberships for a given groupId.
     *
     * @param groupId the id of the group
     * @return a list of GroupMemberships associated with the given groupId
     */
    List<GroupMembership> findByGroupId(Long groupId);
}

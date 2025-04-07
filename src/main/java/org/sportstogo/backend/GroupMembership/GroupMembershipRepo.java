package org.sportstogo.backend.GroupMembership;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupMembershipRepo extends JpaRepository<GroupMembership,GroupMembershipId> {

}

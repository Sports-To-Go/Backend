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

    @Query("SELECT gm FROM GroupMembership gm WHERE gm.userID.uid = :userId")
    List<GroupMembership> findByUserId(@Param("userId") String userId);

    @Query("SELECT gm FROM GroupMembership gm WHERE gm.groupID.id = :groupId")
    List<GroupMembership> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT COUNT(gm) > 0 FROM GroupMembership gm WHERE gm.userID.uid = :userId AND gm.groupID.id = :groupId")
    boolean existsByUserIDAndGroupID(@Param("userId") String userId, @Param("groupId") Long groupId);

    void deleteByUserIDUidAndGroupIDId(String userId, Long groupId);
}
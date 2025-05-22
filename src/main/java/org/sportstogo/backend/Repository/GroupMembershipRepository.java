package org.sportstogo.backend.Repository;

import org.sportstogo.backend.DTOs.GroupDataDTO;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupMemberID> {
    @Query("SELECT COUNT(gm) > 0 FROM GroupMembership gm WHERE gm.userID.uid = :userId AND gm.groupID.id = :groupId")
    boolean existsByUserIDAndGroupID(@Param("userId") String userId, @Param("groupId") Long groupId);

    @Query("""
        SELECT gm
        FROM GroupMembership gm
        WHERE gm.groupID.id = :id
    """)
    List<GroupMembership> findByGroupID(@Param("id") Long id);


    @Query("SELECT gm FROM GroupMembership gm WHERE gm.userID.uid = :userId AND gm.groupID.id = :groupId")
    GroupMembership findByUserIDAndGroupID(@Param("userId") String userId, @Param("groupId") Long groupId);

    @Query("SELECT COUNT(gm) > 0 FROM GroupMembership gm WHERE gm.groupID.id = :groupId")
    boolean existsByGroupID(@Param("groupId") Long groupId);

    @Query(value = """
    SELECT
        g.id AS id,
        g.name AS name,
        g.description AS description
    FROM group_memberships gm JOIN groups g ON gm.group_id = g.id
    WHERE gm.user_id = :uid
    """, nativeQuery = true)
    List<GroupDataDTO> findAllByUserID(@Param("uid") String uid);

    @Query(value = """
    SELECT
        g.id
    FROM group_memberships gm JOIN groups g ON gm.group_id = g.id
    WHERE gm.user_id = :uid
    """, nativeQuery = true)
    List<Long> findAllGroupIdsByUserID(@Param("uid") String uid);
}
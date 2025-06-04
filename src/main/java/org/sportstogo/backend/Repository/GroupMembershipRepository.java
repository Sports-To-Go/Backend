package org.sportstogo.backend.Repository;

import org.sportstogo.backend.DTOs.GroupDataDTO;
import org.sportstogo.backend.DTOs.GroupPreviewDTO;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        g.description AS description,
        g.theme as theme,
        i.url AS imageUrl
    FROM group_memberships gm JOIN groups g ON gm.group_id = g.id
    LEFT JOIN images i ON i.id = g.image_id
    WHERE gm.user_id = :uid
    """, nativeQuery = true)
    List<GroupDataDTO> findAllByUserID(@Param("uid") String uid);


    @Query("SELECT new org.sportstogo.backend.DTOs.GroupPreviewDTO(g.id, g.name, g.description) " +
            "FROM GroupMembership gm " +
            "JOIN gm.groupID g " +
            "WHERE gm.userID.uid = :uid AND gm.groupRole <> 0")
    List<GroupPreviewDTO> findGroupsWhereUserHasElevatedRole(@Param("uid") String uid);

    @Modifying
    @Transactional
    @Query("UPDATE GroupMembership gm SET gm.nickname = :nickname WHERE gm.userID.uid = :userId AND gm.groupID.id = :groupId")
    void updateNickname(@Param("userId") String userId, @Param("groupId") Long groupId, @Param("nickname") String nickname);

}
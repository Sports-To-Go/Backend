package org.sportstogo.backend.Repository;

import jakarta.transaction.Transactional;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, GroupMemberID> {
    @Query("SELECT jr FROM JoinRequest jr WHERE jr.groupID.id = :id")
    List<JoinRequest> findByGroupID(@Param("id") Long id);

    @Query("SELECT j FROM JoinRequest j WHERE j.groupID.id = :groupId AND j.userID.uid = :id")
    JoinRequest findByGroupIDAndUserID(Long groupId, String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM JoinRequest jr WHERE jr.groupID.id = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);
}

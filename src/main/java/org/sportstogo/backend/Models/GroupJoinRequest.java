package org.sportstogo.backend.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.idModels.GroupMembershipId;

import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMembershipId.class)
public class GroupJoinRequest {
    @Id
    private Long userId;
    @Id
    private Long groupId;
    private String motivation;
    private LocalDateTime requestDateTime;
}

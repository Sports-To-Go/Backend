package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.idModels.GroupMembershipId;

import java.time.LocalDateTime;

@Entity
@Table(name = "GroupMemberships")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMembershipId.class)
public class GroupMembership {
    @Id
    private Long userId;
    @Id
    private Long groupId;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private LocalDateTime joinTime;
}

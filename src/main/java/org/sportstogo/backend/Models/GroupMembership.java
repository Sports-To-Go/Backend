package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.idModels.GroupMembershipId;

import java.time.LocalDateTime;

/**
 * Entity representing a membership of a user in a group.
 * Uses a composite primary key of userId and groupId.
 */
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMembershipId.class)
public class GroupMembership {

    /**
     * The ID of the user in the membership (composite key part 1).
     */
    @Id
    private Long userId;

    /**
     * The ID of the group in the membership (composite key part 2).
     */
    @Id
    private Long groupId;

    /**
     * The role of the user within the group (e.g., MEMBER, ADMIN).
     */
    @Column(nullable = false)
    private Role role;

    /**
     * The timestamp of when the user joined the group.
     */
    @Column(nullable = false)
    private LocalDateTime joinTime;
}

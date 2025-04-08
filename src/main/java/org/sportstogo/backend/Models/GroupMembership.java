package org.sportstogo.backend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "GroupMemberships")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GroupMembership {
    @Id
    private GroupMembershipId id;
    private Role role;
    @Column(nullable = false)
    private LocalDateTime joinTime;
    public GroupMembership(GroupMembershipId id, Role role) {
        this.id = id;
        this.role = role;
    }
    public GroupMembership(GroupMembershipId id) {
        this.id = id;
        this.role = Role.member;
    }
}

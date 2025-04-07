package org.sportstogo.backend.GroupMembership;

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
    private GroupMembershipId groupMembershipId;
    private Role role;
    private LocalDateTime joinTime;
    public GroupMembership(Role role, LocalDateTime joinTime) {
        this.role = role;
        this.joinTime = joinTime;
    }
}

package org.sportstogo.backend.GroupMembership;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GroupMembershipId implements Serializable {
    private Long userId;
    private Long groupId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMembershipId mb)) return false;
        return mb.userId.equals(this.userId) && mb.groupId.equals(this.groupId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}

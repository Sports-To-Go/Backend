package org.sportstogo.backend.idModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Models.GroupMembership;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key class for {@link GroupMembership}.
 * This class represents a combination of {@code userId} and {@code groupId}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GroupMembershipId implements Serializable {

    /**
     * The ID of the user (part of the composite key).
     */
    private Long userId;

    /**
     * The ID of the group (part of the composite key).
     */
    private Long groupId;

    /**
     * Compares this object with another {@link GroupMembershipId} for equality.
     * Two GroupMembershipIds are equal if both the userId and groupId match.
     *
     * @param o the object to compare this instance with
     * @return {@code true} if the objects are the same, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMembershipId mb)) return false;
        return mb.userId.equals(this.userId) && mb.groupId.equals(this.groupId);
    }

    /**
     * Generates a hash code based on the userId and groupId.
     * This is needed to ensure proper functionality in hash-based collections (e.g., HashMap).
     *
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}

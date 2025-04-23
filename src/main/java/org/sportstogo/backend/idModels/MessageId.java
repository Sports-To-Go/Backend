package org.sportstogo.backend.idModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Class representing the composite key for the Message entity.
 * This is used as the primary key in the `Message` table.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class MessageId {

    private Long id;
    private Long groupId;
    private Long userId;

    /**
     * Compares this object with another to determine equality.
     * Two `MessageId` objects are considered equal if all their ID values are equal.
     *
     * @param o the object to be compared
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageId that = (MessageId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(userId, that.userId);
    }

    /**
     * Calculates the hash code for the current object.
     * Uses the values of `id`, `groupId`, and `userId` to compute the hash code.
     *
     * @return the hash code for the current object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, userId);
    }
}

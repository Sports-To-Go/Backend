package org.sportstogo.backend.idModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GroupMemberID implements Serializable {
    private String userID;
    private Long groupID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMemberID mb)) return false;
        return mb.userID.equals(this.userID) && mb.groupID.equals(this.groupID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, groupID);
    }
}

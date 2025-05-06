package org.sportstogo.backend.idModels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class MessageID implements Serializable {
    private Long groupID;
    private Long ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageID that)) return false;
        return Objects.equals(groupID, that.groupID) && Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupID, ID);
    }
}

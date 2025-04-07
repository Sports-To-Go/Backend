package org.sportstogo.backend.GroupMembership;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GroupMembershipId implements Serializable {
    private Long userId;
    private Long groupId;
}

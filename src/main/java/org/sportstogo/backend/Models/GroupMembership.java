package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.GroupRole;
import org.sportstogo.backend.idModels.GroupMemberID;

import java.time.LocalDateTime;


@Entity
@Table(name = "group_memberships")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMemberID.class)
public class GroupMembership {
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group groupID;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
    private User userID;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private GroupRole groupRole = GroupRole.member;

    @Column(nullable = false)
    private LocalDateTime joinTime;
    @Column(nullable = true)
    private String nickname;
    @PrePersist
    protected void onCreate() {
        this.joinTime = LocalDateTime.now();
    }
}

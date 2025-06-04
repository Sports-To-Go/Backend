package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.DTOs.JoinRequestDTO;
import org.sportstogo.backend.Service.FirebaseTokenService;
import org.sportstogo.backend.idModels.GroupMemberID;

import java.time.LocalDateTime;

@Entity
@Table(name="join_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMemberID.class)
public class JoinRequest {
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group groupID;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
    private User userID;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    @PrePersist
    protected void onCreate() {
        this.requestTime = LocalDateTime.now();
    }

    public JoinRequestDTO toDTO() {
        JoinRequestDTO dto = new JoinRequestDTO();
        dto.setDisplayName(userID.getDisplayName());
        dto.setId(userID.getUid());
        return dto;
    }
}

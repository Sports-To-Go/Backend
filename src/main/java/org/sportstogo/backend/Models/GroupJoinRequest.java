package org.sportstogo.backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.idModels.GroupMembershipId;

import java.time.LocalDateTime;

/**
 * Entity representing a group join request.
 * This class contains the information related to a user's request to join a group.
 */
@Entity
@Table // Optional, specify a custom table name if necessary
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(GroupMembershipId.class)
public class GroupJoinRequest {

    @Id
    private Long userId;

    @Id
    private Long groupId;

    /**
     * The motivation message provided by the user for joining the group.
     * This field can be null if the user doesn't provide any motivation.
     */
    private String motivation;

    /**
     * The date and time when the join request was made.
     * Typically set to the current date and time when the request is created.
     */
    private LocalDateTime requestDateTime;
}

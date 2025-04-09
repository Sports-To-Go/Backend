package org.sportstogo.backend.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.sportstogo.backend.idModels.MessageId;

import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(MessageId.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Id
    private Long groupId;
    @Id
    private Long userId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime timeSent;
}

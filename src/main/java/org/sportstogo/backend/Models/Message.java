package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Enums.MessageType;
import org.sportstogo.backend.Service.FirebaseTokenService;
import org.sportstogo.backend.idModels.MessageID;

import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(MessageID.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_id_seq", allocationSize = 1)
    private Long ID;

    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group groupID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
    private User userID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type = MessageType.TEXT;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timeSent;

    public MessageDTO toDTO() {
        return new MessageDTO(
                ID,
                groupID.getId(),
                userID.getUid(),
                content,
                timeSent.toString(),
                type
        );
    }
}

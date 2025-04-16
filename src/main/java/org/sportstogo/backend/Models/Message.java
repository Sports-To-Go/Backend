package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.idModels.MessageId;

import java.time.LocalDateTime;

/**
 * Represents a message within a group sent by a user.
 * Each message contains an ID, the group ID, user ID, the message content, and the time the message was sent.
 */
@Entity
@Table(name = "Messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(MessageId.class)
public class Message {

    /**
     * The unique identifier for the message.
     * It is automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * The unique identifier of the group in which the message is sent.
     */
    @Id
    private Long groupId;

    /**
     * The unique identifier of the user who sent the message.
     */
    @Id
    private Long userId;

    /**
     * The content of the message sent by the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String content;

    /**
     * The time when the message was sent.
     * Cannot be null.
     */
    @Column(nullable = false)
    private LocalDateTime timeSent;

}

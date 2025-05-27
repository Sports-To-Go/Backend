package org.sportstogo.backend.Models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MessageType type = MessageType.TEXT;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timeSent;

    @Column(name = "system_event")
    private String systemEvent; // nullable

    @Column(name = "meta_data", columnDefinition = "TEXT")
    private String metaData; // JSON string, nullable

    public MessageDTO toDTO() {
        MessageDTO dto = new MessageDTO();
        dto.setId(ID);
        dto.setGroupID(groupID.getId());
        dto.setContent(content);
        dto.setSenderID(userID.getUid());
        dto.setType(type);
        dto.setTimestamp(timeSent.toString());
        dto.setSystemEvent(this.systemEvent);
        if (this.metaData != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                dto.setMeta(objectMapper.readValue(this.metaData, new TypeReference<>() {}));
            } catch (Exception e) {
                dto.setMeta(null);
            }
        }

        return dto;
    }
}

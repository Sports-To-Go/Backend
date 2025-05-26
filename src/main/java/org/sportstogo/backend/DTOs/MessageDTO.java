package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.MessageType;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class MessageDTO {
    private Long id;
    private Long groupID;
    private String senderID;
    private String content;
    private String timestamp;
    private MessageType type;
}

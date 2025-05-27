package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.MessageType;

import java.util.Map;

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
    private String systemEvent;
    private Map<String, Object> meta;
}

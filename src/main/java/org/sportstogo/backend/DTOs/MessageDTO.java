package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class MessageDTO {
    private Long id;
    private Long groupID;
    private String senderID;
    private String content;
    private String timestamp;
}

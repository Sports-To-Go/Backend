package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<MessageDTO> getMessagesForGroup(Long groupId, LocalDateTime before) {
        List<Message> messages = messageRepository.findRecentByGroupId(groupId, before);
        return messages.stream()
                .map(Message::toDTO)
                .collect(Collectors.toList());
    }
}

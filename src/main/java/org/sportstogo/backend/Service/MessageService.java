package org.sportstogo.backend.Service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
//@NoArgsConstructor
@Getter @Setter
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    public List<Message> getMessages(Long groupId, int numberOfMessages) {
        return messageRepository.findByGroupIdNr(groupId, numberOfMessages);
    }

    public ResponseEntity<?> addMessage(Message message) {
        if (!groupRepository.existsById(message.getGroupId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group with id " + message.getGroupId() + " does not exist");
        } else if (!userRepository.existsById(message.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + message.getUserId() + " does not exist");
        }
        if (message.getTimeSent() == null) {
            message.setTimeSent(LocalDateTime.now());
        }
        messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.OK).body("Message has been saved successfully");

    }


}

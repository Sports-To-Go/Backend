package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

/**
 * Service class responsible for handling business logic related to messages.
 * Provides methods for retrieving and saving messages in the system.
 */
@Service
@AllArgsConstructor
@Getter
@Setter
public class MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    /**
     * Retrieves a list of messages for a specified group.
     *
     * @param groupId the ID of the group for which messages are being fetched
     * @param numberOfMessages the number of messages to retrieve
     * @return a list of messages for the specified group
     */
    public List<Message> getMessages(Long groupId, int numberOfMessages) {
        return messageRepository.findByGroupIdNr(groupId, numberOfMessages);
    }

    /**
     * Adds a new message to the system.
     * Ensures that both the group and user associated with the message exist.
     * If the time sent is not provided, the current timestamp will be used.
     *
     * @param message the message object to be added
     * @return a response entity with the status of the operation
     */
    public ResponseEntity<?> addMessage(Message message) {
        if (!groupRepository.existsById(message.getGroupId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group with id " + message.getGroupId() + " does not exist");
        } else if (!userRepository.existsById(message.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + message.getUserId() + " does not exist");
        }

        // If timeSent is not provided, use the current date and time
        if (message.getTimeSent() == null) {
            message.setTimeSent(LocalDateTime.now());
        }

        messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.OK).body("Message has been saved successfully");
    }
}

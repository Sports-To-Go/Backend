//package org.sportstogo.backend.Controller;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.sportstogo.backend.Models.Message;
//import org.sportstogo.backend.Service.MessageService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Controller class responsible for handling HTTP requests related to messages in the system.
// * Provides endpoints for retrieving and adding messages to a group.
// */
//@RestController
//@RequestMapping(path = "Messages")
//@AllArgsConstructor
//@NoArgsConstructor
//public class MessageController {
//
//    private MessageService messageService;
//
//    /**
//     * Retrieves a list of messages for a specific group.
//     *
//     * @param groupId the ID of the group for which messages are being fetched
//     * @param numberOfMessages the number of messages to retrieve
//     * @return a list of messages belonging to the specified group
//     */
//    @GetMapping
//    public List<Message> getMessages(@RequestParam Long groupId, @RequestParam int numberOfMessages) {
//        return messageService.getMessages(groupId, numberOfMessages);
//    }
//
//    /**
//     * Adds a new message to the system.
//     *
//     * @param message the message object to be added
//     * @return a response entity with the status of the operation
//     */
//    @PostMapping
//    public ResponseEntity<?> addMessage(@RequestBody Message message) {
//        return messageService.addMessage(message);
//    }
//}

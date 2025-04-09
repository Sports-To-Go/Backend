package org.sportstogo.backend.Controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "Messages")
@AllArgsConstructor
@NoArgsConstructor
public class MessageController {
    private MessageService messageService;
    @GetMapping
    public List<Message> getMessages(@RequestParam Long groupId, @RequestParam int numberOfMessages) {
        return messageService.getMessages(groupId, numberOfMessages);
    }
    @PostMapping
    public ResponseEntity<?> addMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }
}

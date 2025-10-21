package com.web.syncspace.controllers.message;

import com.web.syncspace.dto.message.MessageDTO;
import com.web.syncspace.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat.sendMessage")
    public void sendChatMessage(@Payload MessageDTO messageDTO) {
        System.out.println("Reached controller...");
        messageService.saveMessage(messageDTO);
        simpMessagingTemplate.convertAndSendToUser(
                messageDTO.getReceiverUserName(),
                "/queue/messages", messageDTO
        );
    }


}

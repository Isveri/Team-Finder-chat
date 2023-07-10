package com.evi.teamfinderchat.controller;

import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.model.MessageLogsDTO;
import com.evi.teamfinderchat.model.UnreadMessageCountDTO;
import com.evi.teamfinderchat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;


@RestController
@AllArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public MessageDTO send(MessageDTO messageDTO, @DestinationVariable Long groupId) throws Exception {
        return chatService.save(messageDTO, groupId);

    }

    @MessageMapping("/private/{chatId}")
    @SendTo("/topic/private/{chatId}")
    public MessageDTO sendPrivateMessage(MessageDTO messageDTO, @DestinationVariable Long chatId) throws Exception {
        return chatService.savePrivate(messageDTO, chatId);
    }


}

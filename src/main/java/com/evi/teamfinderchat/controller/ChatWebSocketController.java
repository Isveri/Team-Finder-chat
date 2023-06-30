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

    @MessageMapping("/privateChat/{chatId}")
    @SendTo("/topic/privateMessages/{chatId}")
    public MessageDTO sendPrivateMessage(MessageDTO messageDTO, @DestinationVariable Long chatId) throws Exception{
        return chatService.savePrivate(messageDTO,chatId);
    }


    @GetMapping("/api/v1/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getChat(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.getChatMessages(chatId));
    }
    @GetMapping("/api/v1/chatLogs/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getChatLogs(@PathVariable Long groupId){
        return chatService.getChatLogs(groupId);
    }

    @GetMapping("/api/v1/users/chatLogs/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageLogsDTO> getUserChatLogs(@PathVariable Long userId){

        return chatService.getUserChatLogs(userId);
    }

    @GetMapping("/api/v1/deletedGroupLogs/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getDeletedGroupChatLogs(@PathVariable Long groupId) {
        return chatService.getDeletedGroupChatLogs(groupId);
    }

    @PatchMapping("/api/v1/messageRead/{chatId}")
    public ResponseEntity<List<MessageDTO>> setMessageAsRead(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.setMessagesAsRead(chatId));
    }

    @GetMapping("/api/v1/unreadMessages")
    public ResponseEntity<List<UnreadMessageCountDTO>> countUnreadMessages(){
        return ResponseEntity.ok(chatService.countUnreadMessages());
    }

    @PostMapping("/api/v1/create/{groupId}")
    public ResponseEntity<Long> createChat(@PathVariable Long groupId){
        return ResponseEntity.ok(chatService.createChat(groupId));
    }

    @PostMapping("/api/v1/createPrivate")
    public ResponseEntity<Long> createPrivateChat(){
        return ResponseEntity.ok(chatService.createPrivateChat());
    }
}

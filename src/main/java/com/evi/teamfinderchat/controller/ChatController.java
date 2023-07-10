package com.evi.teamfinderchat.controller;

import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.model.MessageLogsDTO;
import com.evi.teamfinderchat.model.UnreadMessageCountDTO;
import com.evi.teamfinderchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageDTO>> getChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChatMessages(chatId));
    }

    @GetMapping("/group/logs/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getChatLogs(@PathVariable Long groupId) {
        return chatService.getChatLogs(groupId);
    }

    @GetMapping("/user/logs/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageLogsDTO> getUserChatLogs(@PathVariable Long userId) {

        return chatService.getUserChatLogs(userId);
    }

    @GetMapping("/group/logs/deleted/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getDeletedGroupChatLogs(@PathVariable Long groupId) {
        return chatService.getDeletedGroupChatLogs(groupId);
    }

    @PatchMapping("/messages/{chatId}")
    public ResponseEntity<List<MessageDTO>> setMessageAsRead(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.setMessagesAsRead(chatId));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<UnreadMessageCountDTO>> countUnreadMessages() {
        return ResponseEntity.ok(chatService.countUnreadMessages());
    }

    @PostMapping("/{groupId}")
    public ResponseEntity<Long> createChat(@PathVariable Long groupId) {
        return ResponseEntity.ok(chatService.createChat(groupId));
    }

    @PostMapping("/private")
    public ResponseEntity<Long> createPrivateChat() {
        return ResponseEntity.ok(chatService.createPrivateChat());
    }
}

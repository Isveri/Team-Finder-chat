package com.evi.teamfinderchat.service;


import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.model.MessageLogsDTO;
import com.evi.teamfinderchat.model.UnreadMessageCountDTO;

import java.util.List;

public interface ChatService {

    MessageDTO save(MessageDTO messageDTO, Long groupId);

    MessageDTO savePrivate(MessageDTO messageDTO, Long chatId);

    List<MessageDTO> setMessagesAsRead(Long chatId);

    List<MessageDTO> getChatMessages(Long chatId);

    List<MessageDTO> getChatLogs(Long groupId);

    List<MessageLogsDTO> getUserChatLogs(Long userId);

    List<UnreadMessageCountDTO> countUnreadMessages();

    List<MessageDTO> getDeletedGroupChatLogs(Long groupId);

    Long createChat(Long groupId);

    Long createPrivateChat();
}

package com.evi.teamfinderchat.service;

import com.evi.teamfinderchat.domain.Chat;
import com.evi.teamfinderchat.domain.UserFriend;
import com.evi.teamfinderchat.domain.Message;
import com.evi.teamfinderchat.domain.MessageStatus;
import com.evi.teamfinderchat.exception.*;
import com.evi.teamfinderchat.mapper.MessageMapper;
import com.evi.teamfinderchat.messaging.NotificationMessagingService;
import com.evi.teamfinderchat.messaging.model.Notification;
import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.model.MessageLogsDTO;
import com.evi.teamfinderchat.model.UnreadMessageCountDTO;
import com.evi.teamfinderchat.repository.*;
import com.evi.teamfinderchat.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.evi.teamfinderchat.utils.UserDetailsHelper.getCurrentUser;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final NotificationMessagingService notificationMessagingService;


    @Transactional
    @Override
    public MessageDTO save(MessageDTO messageDTO, Long groupId) {

        if (messageDTO.getText() != null) {
            User user = getCurrentUser();
            List<Long> groupUsersIds = userRepository.findUsersIds(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
            if (groupUsersIds.contains(user.getId()) || user.getRole().getName().equals("ROLE_ADMIN")) {
                Chat chat = chatRepository.findChatByGroupId(groupId).orElseThrow(() -> new GroupNotFoundException("Chat not found"));
                Message msg = getMessage(messageDTO, chat);
                chat.getMessages().add(msg);
                chatRepository.save(chat);
                return messageMapper.mapMessageToMessageDTO(msg);
            }
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    private Message getMessage(MessageDTO messageDTO, Chat chat) {
        if (!messageDTO.getText().isBlank()) {
            Message msg = messageMapper.mapMessageDTOTOMessage(messageDTO);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            msg.setDate(LocalDateTime.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), formatter));
            msg.setChat(chat);
            msg.setUser(getCurrentUser());
            return msg;
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    @Transactional
    @Override
    public MessageDTO savePrivate(MessageDTO messageDTO, Long chatId) {
        if (messageDTO.getText() != null) {
            Chat chat = chatRepository.findById(chatId).orElseThrow();
            Message msg = getMessage(messageDTO, chat);
            UserFriend userFriend = chat.getUsers().stream().filter((fr -> !getCurrentUser().equals(fr.getUser()))).findAny().orElseThrow(() -> new UserNotFoundException("User doesnt exist"));
            MessageStatus msgStatus = MessageStatus.builder().user(userFriend.getUser()).build();
            msgStatus.setMessage(msg);
            messageStatusRepository.save(msgStatus);
            msg.setStatuses(List.of(msgStatus));
            chat.getMessages().add(msg);
            chatRepository.save(chat);
            notificationMessagingService.sendNotification(Notification.builder().msg("New message").notificationType(Notification.NotificationType.PRIVATE_MESSAGE).userId(userFriend.getUser().getId()).build());
            return messageMapper.mapMessageToMessageDTO(msg);
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    @Override
    public List<MessageDTO> setMessagesAsRead(Long chatId) {

        User user = getCurrentUser();
        List<Message> messageList = messageRepository.findAllNotReadByChatId(chatId, MessageStatus.Status.UNREAD, user.getId());
        messageList.forEach((message -> {
            messageRepository.save(setStatus(user, message));
        }));
        notificationMessagingService.sendNotification(Notification.builder().msg("").notificationType(Notification.NotificationType.PRIVATE_MESSAGE).userId(user.getId()).build());

        return null;
    }

    @Override
    public List<MessageDTO> getChatMessages(Long chatId) {
        return messageRepository.findAllByChatId(chatId)
                .stream()
                .map(messageMapper::mapMessageToMessageDTO)
                .collect(Collectors.toList());
    }

    private Message setStatus(User user, Message message) {
        message.getStatuses()
                .stream()
                .filter(messageStatus ->
                        user.equals(messageStatus.getUser())
                ).findFirst().orElseThrow().setStatus(MessageStatus.Status.READ);
        return message;
    }

    @Override
    public List<MessageDTO> getChatLogs(Long groupId) {
        User user = getCurrentUser();
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            return chatRepository.findChatByGroupId(groupId).orElseThrow(() -> new GroupNotFoundException("Chat not found"))
                    .getMessages()
                    .stream()
                    .map(messageMapper::mapMessageToMessageDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }

    @Override
    public List<MessageLogsDTO> getUserChatLogs(Long userId) {
        User admin = getCurrentUser();

        if (admin.getRole().getName().equals("ROLE_ADMIN")) {
            return messageRepository.findAllByUserIdAndChat_notPrivate(userId, true)
                    .stream()
                    .map(messageMapper::mapMessageToMessageLogsDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }


    @Override
    public List<UnreadMessageCountDTO> countUnreadMessages() {
        List<UnreadMessageCountDTO> unreadMessages = new ArrayList<>();
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        //TODO opcjonalnie można spytac serwis core o liste znajomych na podstawie usera
        user.getUserFriendList().forEach((userFriend -> {
            UnreadMessageCountDTO unreadMessageCountDTO = new UnreadMessageCountDTO();
            unreadMessageCountDTO.setUserId(userFriend.getUser().getId());
            unreadMessageCountDTO.setCount(messageRepository.countAllByStatusesUser(MessageStatus.Status.UNREAD, userFriend.getUser().getId(), userFriend.getChatId()));
            unreadMessages.add(unreadMessageCountDTO);
        }));
        return unreadMessages;
    }

    @Override
    public List<MessageDTO> getDeletedGroupChatLogs(Long groupId) {
        User user = getCurrentUser();
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            Chat chat = chatRepository.findChatByGroupId(groupId).orElseThrow(() -> new ChatNotFoundException("Chat for group with id: " + groupId + " not found"));
            return chat.getMessages()
                    .stream()
                    .map(messageMapper::mapMessageToMessageDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }

    @Override
    public Long createChat(Long groupId) {
        Chat chat = Chat.builder()
                .groupId(groupId)
                .build();

        return chatRepository.save(chat).getId();
    }

    @Override
    public Long createPrivateChat() {
        Chat chat = Chat.builder().notPrivate(false).build();

        return chatRepository.save(chat).getId();
    }
}


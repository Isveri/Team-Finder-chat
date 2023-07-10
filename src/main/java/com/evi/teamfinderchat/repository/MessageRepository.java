package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.domain.Message;
import com.evi.teamfinderchat.domain.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUserIdAndChat_notPrivate(Long id, boolean value);

    @Query("SELECT m FROM Message m JOIN FETCH m.statuses ms WHERE m.chat.id =:chatId AND ms.status=:status AND ms.user.id=:userId")
    List<Message> findAllNotReadByChatId(@Param("chatId") Long chatId, @Param("status") MessageStatus.Status status, @Param("userId") Long userId);

    @Query("SELECT COUNT (m) FROM Message m JOIN m.statuses ms WHERE ms.status=:status AND m.user.id=:userId AND m.chat.id=:chatId ")
    int countAllByStatusesUser(@Param("status") MessageStatus.Status status, @Param("userId") Long userId, @Param("chatId") Long chatId);

    List<Message> findAllByChatId(Long chatId);
}

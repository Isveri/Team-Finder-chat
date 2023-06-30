package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    Optional<Chat> findChatByGroupId(Long groupId);


    @Query("SELECT c FROM Chat c JOIN FETCH c.users WHERE c.id = :id")
    Optional<Chat> findByIdFetch(@Param("id") Long id);


}

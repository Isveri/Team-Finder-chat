package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.domain.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStatusRepository extends JpaRepository<MessageStatus,Long> {
}

package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.domain.Friend;
import com.evi.teamfinderchat.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<List<Friend>> findAllByUser(User user);

}

package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.domain.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<UserFriend, Long> {


}

package com.evi.teamfinderchat.repository;

import com.evi.teamfinderchat.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT user_id from users_groups g where g.group_id = :groupId " ,nativeQuery = true)
    Optional<List<Long>> findUsersIds(Long groupId);

}

package com.evi.teamfinderchat.domain;

import com.evi.teamfinderchat.security.model.User;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Where(clause = "deleted=false")
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User friend;

    private Long chatId;

    @Builder.Default
    private boolean deleted = false;
}

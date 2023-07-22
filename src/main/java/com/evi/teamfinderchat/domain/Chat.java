package com.evi.teamfinderchat.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.MERGE)
    private List<Message> messages;

    @OneToMany(mappedBy = "chatId", cascade = CascadeType.MERGE)
    private List<UserFriend> users;

    @Builder.Default
    private boolean notPrivate = true;
}

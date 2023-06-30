package com.evi.teamfinderchat.domain;

import com.evi.teamfinderchat.security.model.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Builder.Default
    private boolean online = false;

    private Long chatId;
}

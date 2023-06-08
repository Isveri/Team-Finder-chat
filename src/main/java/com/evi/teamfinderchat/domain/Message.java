package com.evi.teamfinderchat.domain;

import com.evi.teamfinderchat.security.model.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @ManyToOne()
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="chat_id")
    private Chat chat;

    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "message")
    private List<MessageStatus> statuses;

}

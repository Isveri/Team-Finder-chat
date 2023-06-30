package com.evi.teamfinderchat.model;

import com.evi.teamfinderchat.domain.MessageStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class MessageStatusDTO {
    private Long id;
    private MessageStatus.Status status;
    private UserMsgDTO user;
}

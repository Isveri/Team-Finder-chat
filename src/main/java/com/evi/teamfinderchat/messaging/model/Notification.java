package com.evi.teamfinderchat.messaging.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    public enum NotificationType {
        REMOVED,
        INFO,
        WARNING,
        FRIENDREQUEST,
        PRIVATE_MESSAGE,
    }

    private Long groupId;
    private Long userId;
    private NotificationType notificationType;
    private String msg;
}

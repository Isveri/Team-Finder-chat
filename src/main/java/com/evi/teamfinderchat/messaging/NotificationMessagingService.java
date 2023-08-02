package com.evi.teamfinderchat.messaging;

import com.evi.teamfinderchat.messaging.model.Notification;

public interface NotificationMessagingService {

    void sendNotification(Notification notification);
}

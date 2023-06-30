package com.evi.teamfinderchat.messaging;

import com.evi.teamfinderchat.messaging.model.Notification;

public interface NotificationMessagingService {

    public void sendNotification(Notification notification);
}

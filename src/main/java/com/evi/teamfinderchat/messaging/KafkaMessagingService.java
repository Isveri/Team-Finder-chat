package com.evi.teamfinderchat.messaging;

import com.evi.teamfinderchat.messaging.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "kafka")
public class KafkaMessagingService implements NotificationMessagingService {

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @Override
    public void sendNotification(Notification notification) {
        kafkaTemplate.send(topic, notification);

    }
}

package com.evi.teamfinderchat.messaging.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.support.converter.MessageConverter;

import com.evi.teamfinderchat.messaging.model.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;


@Component
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "activemq")
public class NotificationMessageConverter implements MessageConverter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificationMessageConverter.class);

    ObjectMapper mapper;

    public NotificationMessageConverter() {
        mapper = new ObjectMapper();
    }

    @Override
    public Message toMessage(Object object, Session session)
            throws JMSException {
        Notification notification = (Notification) object;
        String payload = null;
        try {
            payload = mapper.writeValueAsString(notification);
            LOGGER.info("outbound json='{}'", payload);
        } catch (JsonProcessingException e) {
            LOGGER.error("error converting form person", e);
        }

        TextMessage message = session.createTextMessage();
        message.setText(payload);

        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String payload = textMessage.getText();
        LOGGER.info("inbound json='{}'", payload);

        Notification notification = null;
        try {
            notification = mapper.readValue(payload, Notification.class);
        } catch (Exception e) {
            LOGGER.error("error converting to person", e);
        }

        return notification;
    }
}

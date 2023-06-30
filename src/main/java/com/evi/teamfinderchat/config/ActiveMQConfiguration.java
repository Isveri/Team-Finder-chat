package com.evi.teamfinderchat.config;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

@EnableJms
@Configuration
@ConditionalOnProperty(prefix = "notification", name = "service",havingValue = "activemq")
public class ActiveMQConfiguration {

    @Bean
    public Queue createQueue(){

        return new ActiveMQQueue("notifications");

    }
}

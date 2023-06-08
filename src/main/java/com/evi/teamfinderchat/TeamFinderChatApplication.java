package com.evi.teamfinderchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class TeamFinderChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamFinderChatApplication.class, args);
    }

}

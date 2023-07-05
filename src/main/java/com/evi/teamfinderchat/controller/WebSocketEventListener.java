package com.evi.teamfinderchat.controller;

import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    private Map<String,String> destination = new HashMap<>();
    private Map<String,Map<String,Long>> connectedUsers = new HashMap<>();


    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {

        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = Objects.requireNonNull(headers.getUser()).getName();

        String id = getGroupIdFromHeader(headers);

        Principal principal =  headers.getUser();
        User user = (User) (principal != null ? ((UsernamePasswordAuthenticationToken) principal).getPrincipal() : null);

        Map<String,Long> users = getMapById(id) ;

        if(!users.containsKey(headers.getSessionId())) {
            users.put(headers.getSessionId(), user.getId());
        }
        connectedUsers.put(id,users);
        Set<Long> usersIdToSend = getIds(id);

        createMessage(usersIdToSend, username, "joined",id);
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {

        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        String id = destination.get(event.getSessionId());

        connectedUsers.get(id).remove(headers.getSessionId());

        Set<Long> usersIdToSend = getIds(id);

        createMessage(usersIdToSend, username, "left",id);

    }

    private Set<Long> getIds(String id) {
        Set<Long> usersIdToSend = new HashSet<>();
        for(Map.Entry<String,Long> entry: connectedUsers.get(id).entrySet()){
            usersIdToSend.add(entry.getValue());
        }
        return usersIdToSend;
    }

    private String getGroupIdFromHeader(SimpMessageHeaderAccessor headers){
        List<String> idList = headers.getNativeHeader("destination");
        String[] splited = idList.get(0).split("/");
        String id = splited[splited.length - 1];
        destination.put(headers.getSessionId(), id);
        return id;
    }

    private Map<String,Long> getMapById(String id){
        if(connectedUsers.get(id)==null){
             return new HashMap<>();
        }
        return connectedUsers.get(id);
    }

    private void createMessage(Set<Long> usersIdToSend,String username, String value, String id){
        MessageDTO messageDTO = MessageDTO.builder().text(username +" "+value+" chat").connectedUsers(new ArrayList<>(usersIdToSend)).build();
        messagingTemplate.convertAndSend("/topic/messages/"+id,messageDTO);
    }
}

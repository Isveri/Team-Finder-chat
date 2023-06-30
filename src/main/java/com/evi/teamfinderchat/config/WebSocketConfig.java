package com.evi.teamfinderchat.config;

import com.evi.teamfinderchat.domain.Chat;
import com.evi.teamfinderchat.exception.ChatNotFoundException;
import com.evi.teamfinderchat.exception.GroupNotFoundException;
import com.evi.teamfinderchat.repository.ChatRepository;
import com.evi.teamfinderchat.repository.UserRepository;
import com.evi.teamfinderchat.security.jwt.JwtTokenUtil;
import com.evi.teamfinderchat.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static com.evi.teamfinderchat.utils.UserDetailsHelper.getCurrentUser;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenUtil jwtTokenUtil;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> tokenList = accessor.getNativeHeader("Authorization");
                    String jwt;
                    boolean isPrivateChat = true;

                    List<String> groupIdList = accessor.getNativeHeader("groupId");
                    List<String> chatIdList = accessor.getNativeHeader("chatId");
                    Long groupId = null;
                    Long chatId = null;

                    if (groupIdList != null) {
                        String groupIdString = groupIdList.get(0).substring(0);
                        groupId = Long.valueOf(groupIdString);
                        isPrivateChat = false;
                    }
                    else if(chatIdList != null){
                        String chatIdString = chatIdList.get(0).substring(0);
                        chatId = Long.valueOf(chatIdString);
                    }
                    if (tokenList == null || tokenList.size() < 1) {
                        return message;
                    } else {
                        jwt = tokenList.get(0).substring(7);

                    }
                    String username = jwtTokenUtil.getUsername(jwt);

                    User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    if (jwtTokenUtil.validate(jwt)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        User usr = getCurrentUser();
                        if(!isPrivateChat) {
                            List<Long> groupUsersIds = userRepository.findUsersIds(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
                            if (groupUsersIds.contains(usr.getId()) || usr.getRole().getName().equals("ROLE_ADMIN")) {
                                accessor.setUser(authentication);
                            }
                        }else{
                            Chat chat = chatRepository.findByIdFetch(chatId).orElseThrow(()-> new ChatNotFoundException("Chat doesnt exist"));
                            if(chat.getUsers().stream().filter((friend -> friend.getUser().equals(usr))).findFirst().orElseThrow(null)!=null){
                                accessor.setUser(authentication);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}

package com.evi.teamfinderchat.mapper;

import com.evi.teamfinderchat.domain.Message;
import com.evi.teamfinderchat.model.MessageDTO;
import com.evi.teamfinderchat.model.MessageLogsDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class,MessageStatusMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageMapper {
    public abstract MessageDTO mapMessageToMessageDTO(Message message);

    public abstract Message mapMessageDTOTOMessage(MessageDTO messageDTO);

    @Mapping(target="groupName",source = "chat.groupId")
    public abstract MessageLogsDTO mapMessageToMessageLogsDTO(Message message);
}

package com.evi.teamfinderchat.mapper;

import com.evi.teamfinderchat.domain.MessageStatus;
import com.evi.teamfinderchat.model.MessageStatusDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),uses= MessageMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageStatusMapper {

    public abstract MessageStatusDTO mapMessageStatusToMessageStatusDTO(MessageStatus messageStatus);
    public abstract MessageStatus mapMessageStatusDTOToMessageStatus(MessageStatusDTO messageStatusDTO);
}

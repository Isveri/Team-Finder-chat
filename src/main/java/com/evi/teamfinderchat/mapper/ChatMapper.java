package com.evi.teamfinderchat.mapper;

import com.evi.teamfinderchat.domain.Chat;
import com.evi.teamfinderchat.model.ChatDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ChatMapper {
}

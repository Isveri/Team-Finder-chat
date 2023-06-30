package com.evi.teamfinderchat.mapper;

import com.evi.teamfinderchat.model.UserMsgDTO;
import com.evi.teamfinderchat.security.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class UserMapper {

    public abstract UserMsgDTO mapUserToUserMsgDTO(User user);
}

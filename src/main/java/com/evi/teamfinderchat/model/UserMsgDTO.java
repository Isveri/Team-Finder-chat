package com.evi.teamfinderchat.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserMsgDTO {
    private Long id;
    private String username;
    private RoleDTO role;
}

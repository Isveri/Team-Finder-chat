package com.evi.teamfinderchat.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Setter
@Getter
public class ChatDTO {

    private Long id;
    private List<MessageDTO> messages;
}

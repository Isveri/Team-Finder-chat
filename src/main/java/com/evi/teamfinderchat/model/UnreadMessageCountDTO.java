package com.evi.teamfinderchat.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class UnreadMessageCountDTO {
    private Long id;
    private int count;
    private Long userId;
}

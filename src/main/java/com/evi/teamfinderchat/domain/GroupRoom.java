package com.evi.teamfinderchat.domain;

import com.evi.teamfinderchat.security.model.User;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Where(clause = "deleted=false")
public class GroupRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private int maxUsers;

    @Builder.Default
    private boolean inGameRolesActive = false;

    @Builder.Default
    private boolean open = true;

    @ManyToMany(mappedBy = "groupRooms",cascade = CascadeType.MERGE)
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "groupRoom",cascade = CascadeType.REMOVE)
    private Chat chat;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="groupLeader_id")
    private User groupLeader;

    private String joinCode;

    private String city;

    private boolean deleted = false;
}

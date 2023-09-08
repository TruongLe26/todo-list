package com.leqtr.todolistdemo2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRole {

    @EmbeddedId
    GroupRoleKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    Group group;

    String roles;
}

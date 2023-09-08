package com.leqtr.todolistdemo2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRoleKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "group_id")
    Long groupId;

}

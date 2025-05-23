package com.leqtr.todolist.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group")
    @OrderColumn(name = "group_role_order")
    private List<GroupRole> groupRoles = new ArrayList<>();
}

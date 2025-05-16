package com.leqtr.todolist.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

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
    Set<GroupRole> groupRoles;

//    @OneToMany(mappedBy = "inGroup", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<TodoItem> groupItems;

}

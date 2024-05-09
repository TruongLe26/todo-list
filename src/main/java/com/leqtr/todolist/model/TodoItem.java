package com.leqtr.todolist.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "todoitems")
public class TodoItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false, referencedColumnName = "email")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "in_group", referencedColumnName = "id")
    @JsonManagedReference
    private Group inGroup;

    private boolean complete;

    private Timestamp completedOn;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "email")
    @JsonManagedReference
    private User updUser;

//    private LocalDateTime intendedTime;

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }

}

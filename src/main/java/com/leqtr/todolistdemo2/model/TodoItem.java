package com.leqtr.todolistdemo2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "todoitems")
public class TodoItem {

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

}

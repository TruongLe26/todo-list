package com.leqtr.todolistdemo2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "noti_of", updatable = false)
    @JsonManagedReference
    private User userNoti;

    private String changedBy;

    private long groupId;

    private long todoItemId;

    private String message;

    @CreationTimestamp
    private Timestamp createdOn;

//    private boolean isRead;

    public Notification(User userNoti, String changedBy, long groupId, long todoItemId, String message) {
        this.userNoti = userNoti;
        this.changedBy = changedBy;
        this.groupId = groupId;
        this.todoItemId = todoItemId;
        this.message = message;
    }

}

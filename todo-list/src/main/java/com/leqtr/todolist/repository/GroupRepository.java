package com.leqtr.todolist.repository;

import com.leqtr.todolist.model.Group;
import com.leqtr.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Group getGroupById(long groupId);

    @Query("SELECT g FROM Group g JOIN g.groupRoles gr JOIN gr.user u WHERE u.email = :username ORDER BY g.id")
    List<Group> findGroupsByUserEmail(@Param("username") String username);

    @Query("SELECT gr.user FROM GroupRole gr WHERE gr.id.groupId = :groupId ORDER BY gr.user.id")
    List<User> findUsersByGroupId(@Param("groupId") long groupId);

    @Query("SELECT gr.user FROM GroupRole gr WHERE gr.group.id = :groupId AND gr.roles = 'ADMIN'")
    User findAdminByGroupId(@Param("groupId") long groupId);

    @Query("SELECT CASE WHEN COUNT(gr) > 0 THEN true ELSE false END FROM GroupRole gr " +
            "WHERE gr.group.id = :groupId AND gr.user.id = :userId AND gr.roles = 'ADMIN'")
    boolean isUserAdminInGroup(@Param("groupId") Long groupId, @Param("userId") long userId);
}

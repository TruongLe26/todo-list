package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.model.Group;
import com.leqtr.todolistdemo2.model.TodoItem;
import com.leqtr.todolistdemo2.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {

    Group getGroupById(long groupId);
    List<Group> getGroups();
    List<User> getUsers(long groupId);
    void createGroup(Group group);
    String addNewMemberToGroup(long groupId, String email);
    void leaveGroup(long id);
    String getRoleInGroup(long id);
    void removeUser(long groupId, long id);
    void promoteUser(long groupId, long id);
    User findAdminByGroupId(long groupId);
    boolean isUserAdminInGroup(long groupId, long userId);
    void saveGroupTodoItem(TodoItem todoItem, long id);
    void deleteTodoItemFromGroup(long groupId, long id);
    Page<Group> findPaginatedGroup(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<User> findPaginatedUser(long groupId, int pageNo, int pageSize, String sortField, String sortDirection);
    Page<TodoItem> findPaginatedTodoItemInGroup(long groupId, int pageNo, int pageSize, String sortField, String sortDirection);

}

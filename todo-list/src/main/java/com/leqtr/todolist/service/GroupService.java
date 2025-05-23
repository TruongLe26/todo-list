package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.model.Group;
import com.leqtr.todolist.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {
    Group getGroupById(long groupId);
    List<Group> getGroups();
    List<User> getUsers(long groupId);
    void createGroup(Group group);
    String addNewMemberToGroup(Long groupId, String email);
    void leaveGroup(long id);
    void removeUser(long groupId, long id);
    void promoteUser(long groupId, long id);
    User findAdminByGroupId(Long groupId);
    boolean isUserAdminInGroup(Long groupId, long userId);
    List<TodoItemDTO> getTodoItemsByGroup(Long groupId);
    void saveGroupTodoItem(TodoItemDTO todoItem, Long id);
    void updateTodoItem(TodoItemDTO todoItem);
    void deleteTodoItemFromGroup(long groupId, String id);
    String getRoleInGroup(Long id);
    Page<Group> findPaginatedGroup(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<User> findPaginatedUser(long groupId, int pageNo, int pageSize, String sortField, String sortDirection);
    Page<TodoItemDTO> findPaginatedTodoItemInGroup(Long groupId, int pageNo, int pageSize, String sortField, String sortDirection);

}

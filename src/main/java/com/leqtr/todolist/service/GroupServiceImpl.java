package com.leqtr.todolist.service;

import com.leqtr.todolist.config.SecurityUtil;
import com.leqtr.todolist.model.*;
import com.leqtr.todolist.repository.GroupRepository;
import com.leqtr.todolist.repository.GroupRoleRepository;
import com.leqtr.todolist.repository.TodoItemRepository;
import com.leqtr.todolist.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserRepository userRepository;

    private final TodoItemRepository todoItemRepository;

    private final GroupRepository groupRepository;

    private final GroupRoleRepository groupRoleRepository;

    private final SecurityUtil securityUtil;

    @Override
    public Group getGroupById(long groupId) {
        return groupRepository.getGroupById(groupId);
    }

    @Override
    public List<Group> getGroups() {
        String username = securityUtil.getSessionUser();
        return groupRepository.findGroupsByUserEmail(username);
    }

    @Override
    public List<User> getUsers(long groupId) {
        return groupRepository.findUsersByGroupId(groupId);
    }

    public List<TodoItem> getTodoItemsByGroup(long groupId) {
        return groupRepository.findTodoItemsByGroupId(groupId);
    }

    @Override
    @Transactional
    public void createGroup(Group group) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);

        groupRepository.save(group);

        GroupRoleKey groupRoleKey = GroupRoleKey.builder()
                .userId(user.getId())
                .groupId(group.getId())
                .build();

        GroupRole groupRole = GroupRole.builder()
                .id(groupRoleKey)
                .user(user)
                .group(group)
                .roles(UserAccess.ADMIN_ACCESS)
                .build();

        groupRoleRepository.save(groupRole);

        Set<GroupRole> groupRoles = new HashSet<>();
        groupRoles.add(groupRole);

        group.setGroupRoles(groupRoles);
        user.setGroupRoles(groupRoles);

        group.setGroupRoles(groupRoles);

    }

    @Override
    public String addNewMemberToGroup(long groupId, String email) {
        User curUser = userRepository.findByEmail(securityUtil.getSessionUser());
        User userToAdd = userRepository.findByEmail(email);

        if (userToAdd == null) {
            return "not-found";
        }

        boolean isAdmin = groupRepository.isUserAdminInGroup(groupId, curUser.getId());
        if (!isAdmin) {
            return "not-admin";
        }

        Group group = groupRepository.getGroupById(groupId);
        Set<GroupRole> groupRoles = userToAdd.getGroupRoles();
        for (GroupRole groupRole : groupRoles) {
            GroupRoleKey groupRoleKey = groupRole.getId();
            long userId = groupRoleKey.getUserId();
            if (userId == userToAdd.getId() && groupRole.getGroup().getId() == groupId) {
                return "already-in-group";
            }
        }

        GroupRoleKey newGroupRoleKey = GroupRoleKey.builder()
                .userId(userToAdd.getId())
                .groupId(groupId)
                .build();

        GroupRole newGroupRole = GroupRole.builder()
                .id(newGroupRoleKey)
                .user(userToAdd)
                .group(group)
                .roles(UserAccess.DEFAULT_ROLE)
                .build();

        groupRoleRepository.save(newGroupRole);

        Set<GroupRole> userGroupRole = userToAdd.getGroupRoles();
        userGroupRole.add(newGroupRole);
        userRepository.save(userToAdd);

        Set<GroupRole> groupGroupRole = group.getGroupRoles();
        groupGroupRole.add(newGroupRole);
        groupRepository.save(group);

        return "success";
    }

    @Override
    public void leaveGroup(long id) {
        String username = securityUtil.getSessionUser();
        User curUser = userRepository.findByEmail(username);

        Set<GroupRole> groupRoles = curUser.getGroupRoles();
        for (GroupRole groupRole : groupRoles) {
            if (groupRole.getId().getGroupId() == id) {
                groupRoleRepository.delete(groupRole);
            }
        }
    }

    @Override
    public void removeUser(long groupId, long id) {
        User curUser = userRepository.findByEmail(securityUtil.getSessionUser());
        User user = userRepository.findById(id);

        String role = getRoleInGroup(groupId);
        if (role.equals("ADMIN") && curUser != user) {
            Set<GroupRole> groupRoles = user.getGroupRoles();
            for (GroupRole aGroupRole : groupRoles) {
                if (aGroupRole.getId().getGroupId() == groupId) {
                    groupRoleRepository.delete(aGroupRole);
                }
            }
        }
    }

    @Override
    @Transactional
    public void promoteUser(long groupId, long id) {
        User curUser = userRepository.findByEmail(securityUtil.getSessionUser());
        User user = userRepository.findById(id);

        String role = getRoleInGroup(groupId);
        if (role.equals("ADMIN") && curUser != user) {
//            List<GroupRole> groupRoles = groupRoleRepository.findAll();
            Set<GroupRole> groupRoles = user.getGroupRoles();
            for (GroupRole aGroupRole : groupRoles) {
                if (aGroupRole.getId().getGroupId() == groupId) {
                    aGroupRole.setRoles("ADMIN");
                    groupRoleRepository.save(aGroupRole);
                }
            }
            user.setGroupRoles(groupRoles);

            Set<GroupRole> curUserGroupRoles = curUser.getGroupRoles();
            for (GroupRole curGroupRole : curUserGroupRoles) {
                if (curGroupRole.getId().getGroupId() == groupId) {
                    curGroupRole.setRoles("USER");
                    groupRoleRepository.save(curGroupRole);
                }
            }
            curUser.setGroupRoles(curUserGroupRoles);
        }

    }

    @Override
    public User findAdminByGroupId(long groupId) {
        return groupRepository.findAdminByGroupId(groupId);
    }

    @Override
    public boolean isUserAdminInGroup(long groupId, long userId) {
        return groupRepository.isUserAdminInGroup(groupId, userId);
    }

    @Override
    public void saveGroupTodoItem(TodoItem todoItem, long id) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);
        Group group = groupRepository.getGroupById(id);

        todoItem.setUser(user);
        todoItem.setInGroup(group);
        todoItem.setUpdUser(user);
        todoItemRepository.save(todoItem);
    }

    @Override
    public void deleteTodoItemFromGroup(long groupId, long id) {
        List<TodoItem> todoItems = getTodoItemsByGroup(groupId);

        for (TodoItem todoItem : todoItems) {
            if (todoItem.getId() == id) {
                todoItemRepository.deleteById(id);
            }
        }
    }

    @Override
    public String getRoleInGroup(long id) {
        String username = securityUtil.getSessionUser();
        User curUser = userRepository.findByEmail(username);

        Set<GroupRole> groupRoles = curUser.getGroupRoles();
        for (GroupRole groupRole : groupRoles) {
            if (groupRole.getId().getGroupId() == id && groupRole.getId().getUserId() == curUser.getId()) {
                return groupRole.getRoles();
            }
        }
        return null;
    }

    @Override
    public Page<Group> findPaginatedGroup(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<Group> curListGroup = getGroups();
        return new PageImpl<>(curListGroup.subList(
                (int) pageable.getOffset(),
                Math.min(
                        (int) pageable.getOffset() + pageable.getPageSize(),
                        curListGroup.size()
                )
        ), pageable, curListGroup.size());
    }

    @Override
    public Page<User> findPaginatedUser(long groupId, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<User> curList = getUsers(groupId);
        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min(
                        (int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size()
                )
        ), pageable, curList.size());
    }

    @Override
    public Page<TodoItem> findPaginatedTodoItemInGroup(long groupId, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<TodoItem> curList = getTodoItemsByGroup(groupId);
        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min(
                        (int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size()
                )
        ), pageable, curList.size());
    }

}

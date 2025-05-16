package com.leqtr.todolist.service;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.model.*;
import com.leqtr.todolist.repository.GroupRepository;
import com.leqtr.todolist.repository.GroupRoleRepository;
import com.leqtr.todolist.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final KafkaService kafkaService;
    private final TodoItemClient todoItemClient;
    private final SecurityUtil securityUtil;

    public Group getGroupById(long groupId) {
        return groupRepository.getGroupById(groupId);
    }

    public List<Group> getGroups() {
        String username = securityUtil.getSessionUser();
        return groupRepository.findGroupsByUserEmail(username);
    }

    public List<User> getUsers(long groupId) {
        return groupRepository.findUsersByGroupId(groupId);
    }

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

    public String addNewMemberToGroup(Long groupId, String email) {
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
            if (userId == userToAdd.getId() && groupRole.getGroup().getId().equals(groupId)) {
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

    public void removeUser(long groupId, long id) {
        User curUser = userRepository.findByEmail(securityUtil.getSessionUser());
        User user = userRepository.findById(id)
                .orElseThrow();

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

    @Transactional
    public void promoteUser(long groupId, long id) {
        User curUser = userRepository.findByEmail(securityUtil.getSessionUser());
        User user = userRepository.findById(id)
                .orElseThrow();

        String role = getRoleInGroup(groupId);
        if (role.equals("ADMIN") && curUser != user) {
//            List<GroupRole> groupRoles = groupRoleRepository.findAll();
            Set<GroupRole> groupRoles = user.getGroupRoles();
            for (GroupRole aGroupRole : groupRoles) {
                if (aGroupRole.getId().getGroupId().equals(groupId)) {
                    aGroupRole.setRoles("ADMIN");
                    groupRoleRepository.save(aGroupRole);
                }
            }
            user.setGroupRoles(groupRoles);

            Set<GroupRole> curUserGroupRoles = curUser.getGroupRoles();
            for (GroupRole curGroupRole : curUserGroupRoles) {
                if (curGroupRole.getId().getGroupId().equals(groupId)) {
                    curGroupRole.setRoles("USER");
                    groupRoleRepository.save(curGroupRole);
                }
            }
            curUser.setGroupRoles(curUserGroupRoles);
        }

    }

    public User findAdminByGroupId(Long groupId) {
        return groupRepository.findAdminByGroupId(groupId);
    }

    public boolean isUserAdminInGroup(Long groupId, long userId) {
        return groupRepository.isUserAdminInGroup(groupId, userId);
    }

    public List<TodoItemDTO> getTodoItemsByGroup(Long groupId) {
        List<TodoItemDTO> todoItems = todoItemClient.getTodoItemsByGroupId(groupId);
        System.out.println(todoItems.size());
        return todoItems;
    }

    public void saveGroupTodoItem(TodoItemDTO todoItem, Long id) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);

        todoItem.setGroupId(id);
        todoItem.setCreatedBy(user.getEmail());
        todoItem.setUpdatedBy(user.getEmail());
        kafkaService.createTodoItem(todoItem);
    }

    public void updateTodoItem(TodoItemDTO todoItem) {
        String username = securityUtil.getSessionUser();
        User user = userRepository.findByEmail(username);

        todoItem.setUpdatedBy(user.getEmail());
        todoItemClient.updateTodoItem(todoItem);
    }

    public void deleteTodoItemFromGroup(long groupId, String id) {
        todoItemClient.deleteTodoItemFromGroup(groupId, id);
    }

    public String getRoleInGroup(Long id) {
        String username = securityUtil.getSessionUser();
        User curUser = userRepository.findByEmail(username);

        Set<GroupRole> groupRoles = curUser.getGroupRoles();
        for (GroupRole groupRole : groupRoles) {
            if (Objects.equals(groupRole.getId().getGroupId(), id) && groupRole.getId().getUserId() == curUser.getId()) {
                return groupRole.getRoles();
            }
        }
        return null;
    }

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

    public Page<TodoItemDTO> findPaginatedTodoItemInGroup(Long groupId, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<TodoItemDTO> curList = getTodoItemsByGroup(groupId);
        return new PageImpl<>(curList.subList(
                (int) pageable.getOffset(),
                Math.min(
                        (int) pageable.getOffset() + pageable.getPageSize(),
                        curList.size()
                )
        ), pageable, curList.size());
    }
}

package com.leqtr.todolist.repository;

import com.leqtr.todolist.model.GroupRole;
import com.leqtr.todolist.model.GroupRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRoleRepository extends JpaRepository<GroupRole, GroupRoleKey> {
}

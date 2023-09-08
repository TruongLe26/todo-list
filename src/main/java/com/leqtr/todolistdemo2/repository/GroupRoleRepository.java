package com.leqtr.todolistdemo2.repository;

import com.leqtr.todolistdemo2.model.GroupRole;
import com.leqtr.todolistdemo2.model.GroupRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, GroupRoleKey> {
}

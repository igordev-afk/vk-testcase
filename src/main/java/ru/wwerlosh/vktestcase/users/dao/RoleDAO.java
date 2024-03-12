package ru.wwerlosh.vktestcase.users.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.wwerlosh.vktestcase.users.entities.Role;

public interface RoleDAO extends JpaRepository<Role, Long> {
    Role findByName(String name);

}

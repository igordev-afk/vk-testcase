package ru.wwerlosh.vktestcase.users.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.wwerlosh.vktestcase.users.entities.Privilege;

public interface PrivilegeDAO extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}

package ru.wwerlosh.vktestcase.users.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.wwerlosh.vktestcase.users.entities.User;

public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // return 1 if this pair already exists
    @Query(value = "SELECT COUNT(*) FROM users_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    Long existsUserRole(Long userId, Long roleId);

    @Modifying
    @Query(value = "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void addUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}

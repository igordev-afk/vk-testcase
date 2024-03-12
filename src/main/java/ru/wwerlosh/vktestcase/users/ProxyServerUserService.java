package ru.wwerlosh.vktestcase.users;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.wwerlosh.vktestcase.configuration.security.RoleValidator;
import ru.wwerlosh.vktestcase.users.dao.RoleDAO;
import ru.wwerlosh.vktestcase.users.dao.UserDAO;
import ru.wwerlosh.vktestcase.users.entities.Role;
import ru.wwerlosh.vktestcase.users.entities.User;

@Service
public class ProxyServerUserService {

    private final UserDAO userDAO;

    private final RoleDAO roleDAO;

    private final PasswordEncoder passwordEncoder;

    private final RoleValidator roleValidator;

    @Value("${spring.security.defaultRole}")
    private String defaultRole;

    public ProxyServerUserService(UserDAO userDAO, RoleDAO roleDAO,
                                  PasswordEncoder passwordEncoder, RoleValidator roleValidator) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleValidator = roleValidator;
    }

    public Long createUser(UserDTO userDTO) {
        Role role = roleDAO.findByName(defaultRole);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Set.of(role));

        return userDAO.save(user).getId();
    }

    public User addUserRole(Long id, String newRole) {

        if (!roleValidator.isValidRole(newRole)) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        Optional<User> optionalUser = userDAO.findById(id);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User with id " + id + " not found");

        Role role = roleDAO.findByName(newRole);

        if (userDAO.existsUserRole(id, role.getId()) > 0) {
            return optionalUser.get();
        }

        userDAO.addUserRole(id, role.getId());

        return userDAO.findById(id).orElseThrow();
    }
}

package ru.wwerlosh.vktestcase.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.vktestcase.users.entities.User;

@RestController
public class ProxyServerUserController {

    private final ProxyServerUserService userService;

    public ProxyServerUserController(ProxyServerUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public Long createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PatchMapping("/admin/users/{id}")
    public User addUserRole(@PathVariable("id") Long id, @RequestParam String role) {
        return userService.addUserRole(id, role);
    }
}

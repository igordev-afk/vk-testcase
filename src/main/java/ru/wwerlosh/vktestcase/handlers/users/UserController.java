package ru.wwerlosh.vktestcase.handlers.users;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.vktestcase.handlers.users.dto.UserDTO;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PatchValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PutValidationGroup;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public UserDTO addNewUser(@Validated(PostValidationGroup.class) @RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @DeleteMapping("/users/{id}")
    public Long deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @PutMapping("/users/{id}")
    public UserDTO updateUserById(@PathVariable Long id,
                                  @Validated(PutValidationGroup.class) @RequestBody UserDTO userDTO) {
        return userService.updateUserById(id, userDTO);
    }

    @PatchMapping("/users/{id}")
    public UserDTO updateUserFieldsById(@PathVariable Long id,
                                        @Validated(PatchValidationGroup.class) @RequestBody UserDTO userDTO) {
        return userService.updateUserFieldsById(id, userDTO);
    }



}

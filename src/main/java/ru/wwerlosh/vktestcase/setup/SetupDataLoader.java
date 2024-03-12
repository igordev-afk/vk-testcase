package ru.wwerlosh.vktestcase.setup;

import java.util.Collections;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.wwerlosh.vktestcase.users.entities.Privilege;
import ru.wwerlosh.vktestcase.users.dao.PrivilegeDAO;
import ru.wwerlosh.vktestcase.users.entities.Role;
import ru.wwerlosh.vktestcase.users.dao.RoleDAO;
import ru.wwerlosh.vktestcase.users.entities.User;
import ru.wwerlosh.vktestcase.users.dao.UserDAO;

@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.security.setupRequired}")
    private boolean needToSetup = false;
    private final UserDAO userRepo;
    private final RoleDAO roleRepo;
    private final PrivilegeDAO privilegeRepo;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeHierarchySetup privilegeHierarchySetup;

    public SetupDataLoader(UserDAO userRepo, RoleDAO roleRepo,
                           PrivilegeDAO privilegeRepo, PasswordEncoder passwordEncoder,
                           PrivilegeHierarchySetup privilegeHierarchySetup) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.passwordEncoder = passwordEncoder;
        this.privilegeHierarchySetup = privilegeHierarchySetup;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!needToSetup) {
            return;
        }
        Privilege postCreatePrivilege = createPrivilegeIfNotFound("CREATE_POSTS_PRIVILEGE");
        Privilege postUpdatePrivilege = createPrivilegeIfNotFound("UPDATE_POSTS_PRIVILEGE");
        Privilege postDeletePrivilege = createPrivilegeIfNotFound("DELETE_POSTS_PRIVILEGE");

        Privilege albumCreatePrivilege = createPrivilegeIfNotFound("CREATE_ALBUMS_PRIVILEGE");
        Privilege albumUpdatePrivilege = createPrivilegeIfNotFound("UPDATE_ALBUMS_PRIVILEGE");
        Privilege albumDeletePrivilege = createPrivilegeIfNotFound("DELETE_ALBUMS_PRIVILEGE");

        Privilege userCreatePrivilege = createPrivilegeIfNotFound("CREATE_USERS_PRIVILEGE");
        Privilege userUpdatePrivilege = createPrivilegeIfNotFound("UPDATE_USERS_PRIVILEGE");
        Privilege userDeletePrivilege = createPrivilegeIfNotFound("DELETE_USERS_PRIVILEGE");

        Privilege postReadPrivilege = createPrivilegeIfNotFound("READ_POSTS_PRIVILEGE");
        Privilege albumReadPrivilege = createPrivilegeIfNotFound("READ_ALBUMS_PRIVILEGE");
        Privilege userReadPrivilege = createPrivilegeIfNotFound("READ_USERS_PRIVILEGE");

        Privilege webSocketPrivilege = createPrivilegeIfNotFound("WEBSOCKET_PRIVILEGE");

        createRoleIfNotFound("ROLE_USERS_VIEWER", Set.of(userReadPrivilege));
        createRoleIfNotFound("ROLE_ALBUMS_VIEWER", Set.of(albumReadPrivilege));
        createRoleIfNotFound("ROLE_POSTS_VIEWER", Set.of(postReadPrivilege));
        createRoleIfNotFound("ROLE_USERS_EDITOR", Set.of(userCreatePrivilege,
                userUpdatePrivilege, userDeletePrivilege));
        createRoleIfNotFound("ROLE_ALBUMS_EDITOR", Set.of(albumCreatePrivilege,
                 albumUpdatePrivilege, albumDeletePrivilege));
        createRoleIfNotFound("ROLE_POSTS_EDITOR", Set.of(postCreatePrivilege,
                 postUpdatePrivilege, postDeletePrivilege));
        createRoleIfNotFound("ROLE_ADMIN", Collections.emptySet());
        createRoleIfNotFound("ROLE_CLOWN", Collections.emptySet());
        createRoleIfNotFound("ROLE_WEBSOCKET_USER", Set.of(webSocketPrivilege));


        try {
            privilegeHierarchySetup.setupPrivilegeHierarchy();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }



        Role usersEditor = roleRepo.findByName("ROLE_USERS_EDITOR");
        User userEditor = new User();
        userEditor.setUsername("user_editor");
        userEditor.setPassword(passwordEncoder.encode("test"));
        userEditor.setRoles(Set.of(usersEditor));

        Role usersViewer = roleRepo.findByName("ROLE_USERS_VIEWER");
        User userViewer = new User();
        userViewer.setUsername("user_viewer");
        userViewer.setPassword(passwordEncoder.encode("test"));
        userViewer.setRoles(Set.of(usersViewer));

        Role clown = roleRepo.findByName("ROLE_CLOWN");
        User clownUser = new User();
        clownUser.setUsername("clown");
        clownUser.setPassword(passwordEncoder.encode("test"));
        clownUser.setRoles(Set.of(clown));

        Role postsEditor = roleRepo.findByName("ROLE_POSTS_EDITOR");
        User postsEditorUser = new User();
        postsEditorUser.setUsername("post_editor");
        postsEditorUser.setPassword(passwordEncoder.encode("test"));
        postsEditorUser.setRoles(Set.of(postsEditor));

        Role albumsEditor = roleRepo.findByName("ROLE_ALBUMS_EDITOR");
        User albumsEditorUser = new User();
        albumsEditorUser.setUsername("album_editor");
        albumsEditorUser.setPassword(passwordEncoder.encode("test"));
        albumsEditorUser.setRoles(Set.of(albumsEditor));

        Role admin = roleRepo.findByName("ROLE_ADMIN");
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("test"));
        adminUser.setRoles(Set.of(admin));

        Role websocket = roleRepo.findByName("ROLE_WEBSOCKET_USER");
        User wsUser = new User();
        wsUser.setUsername("websocket");
        wsUser.setPassword(passwordEncoder.encode("test"));
        wsUser.setRoles(Set.of(websocket));

        if (userRepo.findByUsername("admin").isEmpty())
            userRepo.save(adminUser);
        if (userRepo.findByUsername("album_editor").isEmpty())
            userRepo.save(albumsEditorUser);
        if (userRepo.findByUsername("post_editor").isEmpty())
            userRepo.save(postsEditorUser);
        if (userRepo.findByUsername("user_editor").isEmpty())
            userRepo.save(userEditor);
        if (userRepo.findByUsername("user_viewer").isEmpty())
            userRepo.save(userViewer);
        if (userRepo.findByUsername("clown").isEmpty())
            userRepo.save(clownUser);
        if (userRepo.findByUsername("websocket").isEmpty())
            userRepo.save(wsUser);

        needToSetup = false;

    }

    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepo.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepo.save(privilege);
        }
        return privilege;
    }

    Role createRoleIfNotFound(String name, Set<Privilege> privileges) {

        Role role = roleRepo.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepo.save(role);
        }
        return role;

    }
}

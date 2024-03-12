package ru.wwerlosh.vktestcase.setup;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.wwerlosh.vktestcase.users.entities.Role;
import ru.wwerlosh.vktestcase.users.dao.RoleDAO;

@Component
public class PrivilegeHierarchySetup {

    private final RoleHierarchy roleHierarchy;
    private final RoleDAO roleRepo;

    public PrivilegeHierarchySetup(RoleHierarchy roleHierarchy, RoleDAO roleRepo) {
        this.roleHierarchy = roleHierarchy;
        this.roleRepo = roleRepo;
    }

    public void setupPrivilegeHierarchy() throws IllegalAccessException {
        Field field = ReflectionUtils.findField(RoleHierarchyImpl.class, "rolesReachableInOneOrMoreStepsMap");
        field.setAccessible(true);
        Map<String, Set<GrantedAuthority>> roleRepresentation = (Map<String, Set<GrantedAuthority>>) field.get(roleHierarchy);

        for (var entry : roleRepresentation.entrySet()) {
            Role higherRole = roleRepo.findByName(entry.getKey());
            for (var authority : entry.getValue()) {
                Role lowerRole = roleRepo.findByName(authority.getAuthority());
                higherRole.getPrivileges().addAll(lowerRole.getPrivileges());
            }
            roleRepo.save(higherRole);
        }
    }
}

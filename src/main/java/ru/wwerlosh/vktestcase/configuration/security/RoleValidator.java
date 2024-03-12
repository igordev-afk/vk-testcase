package ru.wwerlosh.vktestcase.configuration.security;

import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    @Value("${spring.security.validation.roles}")
    private Set<String> validRoles;

    public boolean isValidRole(String role) {
        return validRoles.contains(role);
    }
}

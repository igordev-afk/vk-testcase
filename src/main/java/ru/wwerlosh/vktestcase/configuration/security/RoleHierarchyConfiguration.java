package ru.wwerlosh.vktestcase.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
public class RoleHierarchyConfiguration {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy =
                        "ROLE_ADMIN > ROLE_USERS_EDITOR\n" +
                        "ROLE_ADMIN > ROLE_POSTS_EDITOR\n" +
                        "ROLE_ADMIN > ROLE_ALBUMS_EDITOR\n" +
                        "ROLE_USERS_EDITOR > ROLE_USERS_VIEWER\n" +
                        "ROLE_POSTS_EDITOR > ROLE_POSTS_VIEWER\n" +
                        "ROLE_ALBUMS_EDITOR > ROLE_ALBUMS_VIEWER\n" +
                        "ROLE_CLOWN > ROLE_USERS_VIEWER\n" +
                        "ROLE_CLOWN > ROLE_POSTS_VIEWER\n" +
                        "ROLE_CLOWN > ROLE_ALBUMS_VIEWER"
                ;
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}

package ru.wwerlosh.vktestcase.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                 .requestMatchers(GET,"/api/users/**").hasAuthority("READ_USERS_PRIVILEGE")
                                 .requestMatchers(POST,"/api/users/**").hasAuthority("CREATE_USERS_PRIVILEGE")
                                 .requestMatchers(DELETE, "/api/users/**").hasAuthority("DELETE_USERS_PRIVILEGE")
                                 .requestMatchers(PUT, "/api/users/**").hasAuthority("UPDATE_USERS_PRIVILEGE")
                                 .requestMatchers(PATCH, "/api/users/**").hasAuthority("UPDATE_USERS_PRIVILEGE")
                                 .requestMatchers(GET, "/api/posts/**").hasAuthority("READ_POSTS_PRIVILEGE")
                                 .requestMatchers(POST,"/api/posts/**").hasAuthority("CREATE_POSTS_PRIVILEGE")
                                 .requestMatchers(DELETE, "/api/posts/**").hasAuthority("DELETE_POSTS_PRIVILEGE")
                                 .requestMatchers(PUT, "/api/posts/**").hasAuthority("UPDATE_POSTS_PRIVILEGE")
                                 .requestMatchers(GET, "/api/albums/**").hasAuthority("READ_ALBUMS_PRIVILEGE")
                                 .requestMatchers(POST,"/api/albums/**").hasAuthority("CREATE_ALBUMS_PRIVILEGE")
                                 .requestMatchers(DELETE, "/api/albums/**").hasAuthority("DELETE_ALBUMS_PRIVILEGE")
                                 .requestMatchers(PUT, "/api/albums/**").hasAuthority("UPDATE_ALBUMS_PRIVILEGE")
                                 .requestMatchers(GET, "/ws").hasAuthority("WEBSOCKET_PRIVILEGE")
                                 .requestMatchers(POST, "/admin/users/**").hasRole("ADMIN")
                                 .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(MyUserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

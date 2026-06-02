package com.strivolabs.strivolabsassessmentjava.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.strivolabs.strivolabsassessmentjava.common.dtos.UserDto;
import com.strivolabs.strivolabsassessmentjava.roles.repositories.RoleRepository;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository users;
    private final RoleRepository roles;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDto userEntity = users.findDtoByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

            List<String> roleNames = roles.findRoleNamesByUserId(userEntity.id());

            String[] authorities = roleNames.stream()
                    .map(role -> "ROLE_" + role)
                    .toArray(String[]::new);

            return User
                    .withUsername(username)
                    .password(userEntity.passwordHash())
                    .authorities(authorities)
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Standard strength parameter is 10 rounds of hashing (ideal blend of security
        // and performance)
        return new BCryptPasswordEncoder(10);
    }
}
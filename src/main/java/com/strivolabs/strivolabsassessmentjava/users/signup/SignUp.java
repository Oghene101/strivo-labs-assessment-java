package com.strivolabs.strivolabsassessmentjava.users.signup;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.constants.Roles;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.roles.repositories.RoleRepository;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;
import com.strivolabs.strivolabsassessmentjava.users.entities.UserRole;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public class SignUp {

        public static record CommandImpl(
                        String firstName,
                        String lastName,
                        String email,
                        String password) implements Command<Response> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Response> {

                private final UserRepository users;
                private final RoleRepository roles;
                private final UserRoleRepository userRoles;
                private final PasswordEncoder passwordEncoder;

                @Override
                @Transactional
                public Response handle(CommandImpl command) {
                        String email = command.email().trim().toLowerCase();

                        if (users.existsByEmail(email)) {
                                throw ApiException.conflict(new ApiError("User.Error", "email already exists"));
                        }

                        UUID roleId = roles.findIdByName(Roles.USER)
                                        .orElseThrow(() -> ApiException
                                                        .notFound(new ApiError("User.Error", "user role not found")));

                        String trimmedFirstName = command.firstName().trim();
                        String trimmedLastName = command.lastName().trim();
                        String fullName = String.format("%s %s", trimmedFirstName, trimmedLastName);

                        var user = User.create(
                                        trimmedFirstName,
                                        trimmedLastName,
                                        email,
                                        passwordEncoder.encode(command.password()),
                                        fullName);

                        users.save(user);

                        UserRole userRole = UserRole.create(
                                        user.getId(),
                                        roleId,
                                        fullName);

                        userRoles.save(userRole);

                        return new Response(user.getId());
                }
        }
}

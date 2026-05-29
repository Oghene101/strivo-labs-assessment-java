package com.strivolabs.strivolabsassessmentjava.auth.signin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.auth.Repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.Repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.dtos.UserDto;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.common.security.JwtService;
import com.strivolabs.strivolabsassessmentjava.common.security.dtos.TokenResponse;
import com.strivolabs.strivolabsassessmentjava.roles.repositories.RoleRepository;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public class SignIn {

        public static record CommandImpl(
                        String email,
                        String password) implements Command<Response> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Response> {

                private final UserRepository users;
                private final RoleRepository roles;
                private final PasswordEncoder passwordEncoder;
                private final JwtService jwt;
                private final SessionRepository sessions;
                private final RefreshTokenRepository refreshTokens;

                @Override
                @Transactional
                public Response handle(CommandImpl command) {
                        String email = command.email().toLowerCase().trim();

                        UserDto user = users.findByEmail(email)
                                        .orElseThrow(() -> ApiException
                                                        .badRequest(new ApiError("Auth.Error",
                                                                        "invalid email or password")));

                        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
                                throw ApiException.badRequest(new ApiError("Auth.Error", "invalid email or password"));
                        }

                        List<String> roleNames = roles.findRoleNamesByUserId(user.id());

                        sessions.revoke(user.id());
                        refreshTokens.revoke(user.id());
                        TokenResponse tokenResponse = jwt.generateToken(user, roleNames);


                        return new Response(tokenResponse);
                }
        }
}

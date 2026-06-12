package com.strivolabs.strivolabsassessmentjava.auth.changepassword;

import java.time.OffsetDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.auth.repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.security.AuthService;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public final class ChangePassword {

        public static record CommandImpl(
                        String oldPassword,
                        String newPassword) implements Command<Void> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Void> {

                private final AuthService auth;
                private final UserRepository users;
                private final PasswordEncoder passwordEncoder;
                private final SessionRepository sessions;
                private final RefreshTokenRepository refreshTokens;

                @Override
                @Transactional
                public Void handle(CommandImpl command) {
                        String email = auth.getSignedInUserEmail();

                        User user = users.findByEmail(email)
                                        .orElseThrow(() -> ApiException
                                                        .badRequest(new ApiError("Auth.Error", "Invalid request")));

                        if (!passwordEncoder.matches(command.oldPassword(), user.getPasswordHash())) {
                                throw ApiException.badRequest(new ApiError("Auth.Error", "Invalid request"));
                        }

                        String newPasswordHash = passwordEncoder.encode(command.newPassword);
                        if (passwordEncoder.matches(command.newPassword(), user.getPasswordHash())) {
                                throw ApiException.badRequest(new ApiError("Auth.Error", "Password reuse detected"));
                        }

                        user.changePassword(newPasswordHash);
                        users.save(user);

                        refreshTokens.revoke(user.getId(), user.getId().toString(), OffsetDateTime.now());
                        sessions.revoke(user.getId(), user.getId().toString(), OffsetDateTime.now());

                        return null;
                }
        }
}

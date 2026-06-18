package com.strivolabs.strivolabsassessmentjava.auth.confirmemail;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.auth.errors.AuthErrors;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.security.JwtService;
import com.strivolabs.strivolabsassessmentjava.security.enums.TokenPurpose;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public final class ConfirmEmail {

        public static record CommandImpl(
                        String email,
                        String token) implements Command<Void> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Void> {

                private final UserRepository users;
                private final JwtService jwt;

                @Override
                @Transactional
                public Void handle(CommandImpl command) {
                        String email = URLDecoder.decode(command.email(), StandardCharsets.UTF_8).trim().toLowerCase();
                        String token = URLDecoder.decode(command.token(), StandardCharsets.UTF_8);

                        Claims claims = jwt.verifyOnetimeToken(token, TokenPurpose.EMAIL_CONFIRMATION);

                        if (!claims.get("email", String.class).equals(email)) {
                                throw ApiException.badRequest(AuthErrors.INVALID_TOKEN_OR_EMAIL);
                        }

                        UUID userId = UUID.fromString(claims.getSubject());

                        User user = users.findById(userId).orElseThrow(() -> ApiException
                                        .badRequest(AuthErrors.INVALID_TOKEN_OR_EMAIL));

                        if (!user.getEmail().equals(email)) {
                                throw ApiException.badRequest(AuthErrors.INVALID_TOKEN_OR_EMAIL);
                        }

                        if (user.isEmailConfirmed()) {
                                throw ApiException.conflict(AuthErrors.EMAIL_ALREADY_CONFIRMED);
                        }

                        user.confirmEmail(user.getFirstName(), email, user.getFirstName() + " " + user.getLastName());

                        users.save(user);

                        return null;
                }
        }
}

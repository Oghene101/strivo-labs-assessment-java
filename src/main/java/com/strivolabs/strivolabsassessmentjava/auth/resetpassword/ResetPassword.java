package com.strivolabs.strivolabsassessmentjava.auth.resetpassword;

import org.springframework.security.crypto.password.PasswordEncoder;
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
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public final class ResetPassword {

        public static record CommandImpl(
                        String token,
                        String newPassword) implements Command<Void> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Void> {

                private final JwtService jwt;
                private final UserRepository users;
                private final PasswordEncoder passwordEncoder;

                @Override
                @Transactional
                public Void handle(CommandImpl command) {
                        Claims claims;
                        try {
                                claims = jwt.verifyOnetimeToken(command.token(), TokenPurpose.PASSWORD_RESET);
                        } catch (JwtException e) {
                                throw ApiException.badRequest(AuthErrors.INVALID_TOKEN_OR_PASSWORD);
                        }

                        String email = claims.get("email", String.class);

                        User user = users.findByEmail(email)
                                        .orElseThrow(() -> ApiException
                                                        .badRequest(AuthErrors.INVALID_TOKEN_OR_PASSWORD));

                        user.changePassword(passwordEncoder.encode(command.newPassword()));

                        users.save(user);

                        return null;
                }
        }
}

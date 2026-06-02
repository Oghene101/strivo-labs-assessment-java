package com.strivolabs.strivolabsassessmentjava.auth.signout;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.strivolabs.strivolabsassessmentjava.auth.repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

public final class SignOut {

    public static record CommandImpl() implements Command<Void> {

    }

    @Component
    @RequiredArgsConstructor
    public static class Handler implements CommandHandler<CommandImpl, Void> {

        private final RefreshTokenRepository refreshTokens;
        private final SessionRepository sessions;

        private static final String HANDLER_NAME = SignOut.class.getSimpleName() + "."
                + SignOut.Handler.class.getSimpleName();

        @Override
        @Transactional
        public Void handle(CommandImpl command) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth.getDetails() instanceof Claims claims) {
                String userIdStr = claims.get("sub", String.class);

                UUID userId = UUID.fromString(userIdStr);

                refreshTokens.revoke(userId, HANDLER_NAME);
                sessions.revoke(userId, HANDLER_NAME);
            } else {
                throw ApiException.unauthorized(new ApiError("Auth.Error", "Missing token claims context"));
            }

            return null;
        }

    }

}

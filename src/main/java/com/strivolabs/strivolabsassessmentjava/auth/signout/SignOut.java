package com.strivolabs.strivolabsassessmentjava.auth.signout;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.strivolabs.strivolabsassessmentjava.auth.repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.security.AuthService;

import lombok.RequiredArgsConstructor;

public final class SignOut {

    public static record CommandImpl() implements Command<Void> {

    }

    @Component
    @RequiredArgsConstructor
    public static class Handler implements CommandHandler<CommandImpl, Void> {

        private final AuthService auth;
        private final RefreshTokenRepository refreshTokens;
        private final SessionRepository sessions;

        private static final String HANDLER_NAME = SignOut.class.getSimpleName() + "."
                + SignOut.Handler.class.getSimpleName();

        @Override
        @Transactional
        public Void handle(CommandImpl command) {
            UUID userId = UUID.fromString(auth.getSignedInUserId());

            refreshTokens.revoke(userId, HANDLER_NAME, OffsetDateTime.now());
            sessions.revoke(userId, HANDLER_NAME, OffsetDateTime.now());

            return null;
        }

    }

}

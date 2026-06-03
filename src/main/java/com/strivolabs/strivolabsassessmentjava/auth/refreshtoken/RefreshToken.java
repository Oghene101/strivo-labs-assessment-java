package com.strivolabs.strivolabsassessmentjava.auth.refreshtoken;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.strivolabs.strivolabsassessmentjava.auth.dtos.RefreshTokenValidationDto;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.roles.repositories.RoleRepository;
import com.strivolabs.strivolabsassessmentjava.security.HashingService;
import com.strivolabs.strivolabsassessmentjava.security.JwtService;
import com.strivolabs.strivolabsassessmentjava.security.dtos.HashResponse;
import com.strivolabs.strivolabsassessmentjava.security.dtos.TokenResponse;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

public final class RefreshToken {

        public static record CommandImpl(
                        String accessToken,
                        String refreshToken) implements Command<Response> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Response> {

                private final JwtService jwt;
                private final HashingService hashing;
                private final RefreshTokenRepository refreshTokens;
                private final SessionRepository sessions;
                private final UserRepository users;
                private final RoleRepository roles;

                private static final String HANDLER_NAME = RefreshToken.class.getSimpleName() + "."
                                + RefreshToken.Handler.class.getSimpleName();

                @Override
                @Transactional
                public Response handle(CommandImpl command) {
                        Claims claims = jwt.getClaimsFromExpiredToken(command.accessToken());

                        HashResponse hashResponse = hashing.compute(command.refreshToken());
                        RefreshTokenValidationDto rerfeshTokenValidation = refreshTokens
                                        .findByTokenHash(hashResponse.hash(), OffsetDateTime.now())
                                        .orElseThrow(() -> ApiException.unauthorized(
                                                        new ApiError("Auth.Error", "invalid token")));

                        UUID userId = UUID.fromString(claims.getSubject());
                        UUID sessionId = UUID.fromString(claims.get("sid", String.class));

                        if (!userId.equals(rerfeshTokenValidation.userId())
                                        || !sessionId.equals(rerfeshTokenValidation.sessionId())) {
                                throw ApiException.unauthorized(
                                                new ApiError("Auth.Error", "invalid token"));
                        }

                        if (!sessions.isActive(sessionId, OffsetDateTime.now())) {
                                throw ApiException.unauthorized(
                                                new ApiError("Auth.Error", "invalid token"));
                        }

                        refreshTokens.revoke(userId, HANDLER_NAME, OffsetDateTime.now());

                        User user = users.findById(userId)
                                        .orElseThrow(() -> ApiException.notFound(
                                                        new ApiError("Auth.Error", "user not found")));

                        List<String> roleNames = roles.findRoleNamesByUserId(userId);

                        TokenResponse token = jwt.generateTokenForExistingSession(user, roleNames, sessionId);

                        return new Response(token);
                }
        }
}

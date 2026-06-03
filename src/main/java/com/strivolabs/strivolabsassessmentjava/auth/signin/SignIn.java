package com.strivolabs.strivolabsassessmentjava.auth.signin;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.strivolabs.strivolabsassessmentjava.auth.repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.ApiError;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.Command;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.CommandHandler;
import com.strivolabs.strivolabsassessmentjava.common.exceptions.ApiException;
import com.strivolabs.strivolabsassessmentjava.common.utils.DateTimeUtils;
import com.strivolabs.strivolabsassessmentjava.roles.repositories.RoleRepository;
import com.strivolabs.strivolabsassessmentjava.security.JwtService;
import com.strivolabs.strivolabsassessmentjava.security.dtos.TokenResponse;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;
import com.strivolabs.strivolabsassessmentjava.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

public final class SignIn {

        public static record CommandImpl(
                        String email,
                        String password) implements Command<Response> {

        }

        @Component
        @RequiredArgsConstructor
        public static class Handler implements CommandHandler<CommandImpl, Response> {

                private final PlatformTransactionManager transactionManager;
                private final UserRepository users;
                private final RoleRepository roles;
                private final PasswordEncoder passwordEncoder;
                private final JwtService jwt;
                private final SessionRepository sessions;
                private final RefreshTokenRepository refreshTokens;

                @Value("${security.lockout.max-failed-attempts}")
                private int maxFailedAttempts;
                @Value("${security.lockout.base-lockout-in-minutes}")
                private int baseLockoutInMin;
                @Value("${security.lockout.multiplier}")
                private double lockoutMultiplier;
                @Value("${security.lockout.max-lockout-minutes}")
                private int maxLockoutMin;

                private static final String HANDLER_NAME = SignIn.class.getSimpleName() + "."
                                + SignIn.Handler.class.getSimpleName();

                @Override
                @Transactional
                public Response handle(CommandImpl command) {
                        String email = command.email().toLowerCase().trim();

                        User user = users.findByEmail(email)
                                        .orElseThrow(() -> ApiException
                                                        .badRequest(new ApiError("Auth.Error",
                                                                        "invalid email or password")));

                        if (user.isLocked()) {
                                throw ApiException.tooManyRequests(
                                                new ApiError("Auth.Error", "account locked until "
                                                                + DateTimeUtils.format(user.getLockoutEnd())));
                        }

                        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
                                handleFailedSignIn(user);
                        }

                        user.unlock(HANDLER_NAME);

                        List<String> roleNames = roles.findRoleNamesByUserId(user.getId());

                        users.save(user);
                        refreshTokens.revoke(user.getId(), HANDLER_NAME, OffsetDateTime.now());
                        sessions.revoke(user.getId(), HANDLER_NAME, OffsetDateTime.now());
                        TokenResponse tokenResponse = jwt.generateToken(user, roleNames);

                        return new Response(tokenResponse);
                }

                private void handleFailedSignIn(User user) {
                        user.recordFailedAccess(HANDLER_NAME);

                        if (user.getAccessFailedCount() < maxFailedAttempts) {
                                saveUserState(user);

                                throw ApiException.badRequest(
                                                new ApiError("Auth.Error", "invalid email or password"));
                        }

                        // Progressive lockout: Base * Multiplier^LockoutCount
                        // LockoutCount tracks how many times the user has been locked out (not reset on
                        // unlock)
                        int lockoutMinutes = (int) (baseLockoutInMin
                                        * Math.pow(lockoutMultiplier, user.getLockoutCount()));
                        lockoutMinutes = Math.min(lockoutMinutes, maxLockoutMin);

                        user.lockUntil(OffsetDateTime.now().plusMinutes(lockoutMinutes), HANDLER_NAME);

                        saveUserState(user);

                        throw ApiException.tooManyRequests(new ApiError("Auth.Error",
                                        "account locked until " + DateTimeUtils.format(user.getLockoutEnd())));
                }

                private void saveUserState(User user) {
                        TransactionTemplate template = new TransactionTemplate(transactionManager);
                        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                        template.execute(status -> {
                                users.saveAndFlush(user);
                                return null;
                        });

                }
        }

}
